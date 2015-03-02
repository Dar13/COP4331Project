package com.mygdx.handlers;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.net.NetworkInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Abstraction away from low-level networking for use in the game states.
 */
public class NetworkManager implements Runnable
{
    public static final int SERVER_PORT = 0xDDD;
    public static final int MAX_CLIENTS = 4;

    public enum ConnectionMode
    {
        WIFI_P2P(1),
        WIFI_LAN(2),
        DATA_4G(3),
        NONE(-1);

        public int index;
        ConnectionMode(int idx)
        {
            index = idx;
        }

        public static ConnectionMode fromIndex(int idx)
        {
            switch(idx)
            {
            case -1:
                return NONE;
            case 1:
                return WIFI_P2P;
            case 2:
                return WIFI_LAN;
            case 3:
                return DATA_4G;
            default:
                return null;
            }
        }
    }

    protected AtomicBoolean initialized;
    protected Boolean ready;

    // server
    protected Boolean isServer;
    protected Server server;
    protected ServerSocket serverSocket;
    protected ArrayList<Socket> connections;
    protected int expectedAmountClients;

    // client
    protected Client client;
    protected Socket clientSocket;
    protected InetAddress hostAddress;

    // techniques of connecting
    protected ConnectionMode preferredMode;
    protected ConnectionMode fallbackMode;
    protected HashMap<ConnectionMode, NetworkInterface> networkInterfaces;
    protected NetworkInterface networkInterface;

    // threading stuff
    protected ReentrantReadWriteLock mutex;

    public NetworkManager(HashMap<ConnectionMode, NetworkInterface> modes)
    {
        networkInterfaces = modes;
        initialized = new AtomicBoolean(false);
        ready = false;

        // this is the closest to a traditional mutex I could find.
        // allows multiple reads at one time while only allowing one write lock.
        mutex = new ReentrantReadWriteLock(true);
    }

    public HashMap<ConnectionMode, NetworkInterface> getNetworkImpls()
    {
        mutex.readLock().lock();
        try
        {
            return networkInterfaces;
        }
        finally
        {
            mutex.readLock().unlock();
        }
    }

    public boolean initialize(Boolean isServer,
                              ConnectionMode primaryMode,
                              ConnectionMode secondaryMode)
    {
        mutex.writeLock().lock();
        try
        {
            // secondary mode is allowed to be null
            if (isServer == null ||
                primaryMode == null)
            {
                initialized.set(false);
                return false;
            }

            this.isServer = isServer;
            this.preferredMode = primaryMode;
            this.fallbackMode = secondaryMode;

            // this can't be null so ignore any warnings that it can be.
            if (isServer)
            {
                connections = new ArrayList<>();
                try
                {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                }
                catch (IOException e)
                {
                    System.out.println("NET: Exception occurred during ServerSocket creation.");
                    e.printStackTrace();
                    initialized.set(false);
                    return false;
                }
            }
            else
            {
                clientSocket = new Socket();
            }

            networkInterface = networkInterfaces.get(primaryMode);

            if (networkInterface != null)
            {
                networkInterface.setup();

                // check to see if primary mode failed to setup properly
                if (!networkInterface.isReady())
                {
                    // switch to fallback connection mode
                    networkInterface = networkInterfaces.get(fallbackMode);

                    if (networkInterface == null)
                    {
                        // invalid fallback
                        System.out.println("NET: Selected fallback connection mode is not supported");
                        return false;
                    }
                    else
                    {
                        networkInterface.setup();
                        if (!networkInterface.isReady())
                        {
                            // fallback failed
                            System.out.println("NET: Fallback mode failed to initialize");
                            initialized.set(false);
                            return false;
                        }
                    }
                }
            }
            else
            {
                System.out.println("NET: Selected connection mode is not supported.");
                initialized.set(false);
                return false;
            }

            initialized.set(true);
            return true;
        }
        finally
        {
            mutex.writeLock().unlock();
        }
    }

    public boolean isInitialized()
    {
        mutex.readLock().lock();
        try
        {
            return initialized.get();
        }
        finally
        {
            mutex.readLock().unlock();
        }
    }

    public void setExpectedAmountClients(int amount)
    {
        expectedAmountClients = amount;
    }

    public int getExpectedAmountClients()
    {
        mutex.readLock().lock();
        try
        {
            return expectedAmountClients;
        }
        finally
        {
            mutex.readLock().unlock();
        }
    }

    public synchronized void setHostAddress(InetAddress addr)
    {
        mutex.writeLock().lock();
        try
        {
            hostAddress = addr;
        }
        finally
        {
            mutex.writeLock().unlock();
        }
    }

    public synchronized InetAddress getHostAddress()
    {
        mutex.readLock().lock();
        try
        {
            return hostAddress;
        }
        finally
        {
            mutex.readLock().unlock();
        }
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run()
    {
        // wait for initialization
        while(!isInitialized())
        {
            // sleep for 5 milliseconds
            try
            {
                Thread.sleep(50);
            }
            catch(InterruptedException e)
            {
                // not really sure how to handle this.
                // terminate?
                System.out.println("NET: Interrupt caught while waiting for initialization.");
                return;
            }
        }

        boolean run = true;

        while(run)
        {
            if (!ready)
            {
                if (isServer)
                {
                    mutex.writeLock().lock();
                    try
                    {
                        run = setupServer();
                        if (!run)
                        {
                            continue;
                        }
                    }
                    finally
                    {
                        mutex.writeLock().unlock();
                    }
                }
                else
                {
                    mutex.writeLock().lock();
                    try
                    {
                        run = setupClient();
                        if(!run)
                        {
                            continue;
                        }
                    }
                    finally
                    {
                        mutex.writeLock().unlock();
                    }
                }

                // if it's made it this far, everything should be ready.
                ready = true;
            }

            if (isServer)
            {
                // server is sending packets out and waiting for incoming connections.
                if(expectedAmountClients != connections.size())
                {
                    Socket connection = null;
                    try
                    {
                        connection = serverSocket.accept();
                    }
                    catch(IOException e)
                    {
                        connection = null;
                        System.out.println("NET: Exception occurred during server acceptance of connection.");
                    }

                    if(connection != null)
                    {

                    }
                }
            }
            else
            {
                // client is waiting for packets to come in.
            }
        }
    }

    public boolean syncReady()
    {
        return false;
    }

    private boolean setupServer()
    {
        // bind server socket to the port and do other initialization as needed.
        try
        {
            serverSocket.bind(new InetSocketAddress(SERVER_PORT));
        }
        catch (IOException e)
        {
            System.out.println("NET: ServerSocket failed to bind to port " + SERVER_PORT);
            e.printStackTrace();
            return false; // non-recoverable error so terminate the thread
        }

        return true;
    }

    private boolean setupClient()
    {
        // if hostAddress is set, attempt to connect. Otherwise wait for it to be set.
        if (getHostAddress() != null && !clientSocket.isConnected())
        {
            try
            {
                clientSocket.bind(null);
                clientSocket.connect(new InetSocketAddress(hostAddress, SERVER_PORT), 500);
            }
            catch (SocketTimeoutException et)
            {
                System.out.println("NET: Client timed out trying to connect to host. Retrying...");
            }
            catch (IOException e)
            {
                System.out.println("NET: Exception during client connection. Not retrying.");
                e.printStackTrace();
                return false; // non-recoverable error so terminate the thread
            }
        }

        return true;
    }
}
