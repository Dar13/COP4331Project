package com.mygdx.handlers;

import com.mygdx.net.NetworkInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Abstraction away from low-level networking for use in the game states.
 */
public class NetworkManager implements Runnable
{
    public static final int SERVER_PORT = 0xDDD;

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

    protected Boolean initialized;
    protected Boolean ready;

    // server
    protected Boolean isServer;
    protected ServerSocket serverSocket;
    protected ArrayList<Socket> connections;

    // client
    protected Socket clientSocket;
    protected InetAddress hostAddress;

    // techniques of connecting
    protected ConnectionMode preferredMode;
    protected ConnectionMode fallbackMode;
    protected HashMap<ConnectionMode, NetworkInterface> networkInterfaces;
    protected NetworkInterface networkInterface;

    public NetworkManager()
    {
        initialized = initialize();
        ready = false;
    }

    /**
     * Takes possession of the hash map containing all network interface implementations.
     * @param modes
     */
    public void setNetworkImpls(HashMap<ConnectionMode, NetworkInterface> modes)
    {
        networkInterfaces = modes;
    }

    public HashMap<ConnectionMode, NetworkInterface> getNetworkImpls()
    {
        return networkInterfaces;
    }

    protected boolean initialize()
    {
        return initialize(null, null, null);
    }

    protected boolean initialize(Boolean isServer,
                                 ConnectionMode primaryMode,
                                 ConnectionMode secondaryMode)
    {
        this.isServer = isServer;
        this.preferredMode = primaryMode;
        this.fallbackMode = secondaryMode;

        // this should never happen.
        assert(isServer == null);

        // this can't be null so ignore any warnings that it can be.
        if (isServer)
        {
            try
            {
                serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
            }
            catch (IOException e)
            {
                System.out.println("NET: Exception occurred during ServerSocket creation.");
                e.printStackTrace();
                return false;
            }
        }
        else
        {
            clientSocket = new Socket();
        }

        networkInterface = networkInterfaces.get(primaryMode);

        if(networkInterface != null)
        {
            networkInterface.setup();

            // check to see if primary mode failed to setup properly
            if(!networkInterface.isReady())
            {
                // switch to fallback connection mode
                networkInterface = networkInterfaces.get(fallbackMode);

                if(networkInterface == null)
                {
                    // invalid fallback
                    System.out.println("NET: Selected fallback connection mode is not supported");
                    return false;
                }
                else
                {
                    networkInterface.setup();
                    if(!networkInterface.isReady())
                    {
                        // fallback failed
                        System.out.println("NET: Fallback mode failed to initialize");
                        return false;
                    }
                }
            }
        }
        else
        {
            System.out.println("NET: Selected connection mode is not supported.");
            return false;
        }

        return true;
    }

    public boolean isInitialized()
    {
        return (initialized == null) ? false : initialized;
    }

    public synchronized void setHostAddress(InetAddress addr)
    {
        hostAddress = addr;
    }

    public synchronized InetAddress getHostAddress()
    {
        return hostAddress;
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
        if(!isInitialized())
        {
            System.out.println("NET: NetworkManager is not initialized");
            return;
        }

        if(!ready)
        {
            if(isServer)
            {
                // bind server socket to the port and do other initialization as needed.
                try
                {
                    serverSocket.bind(new InetSocketAddress(SERVER_PORT));
                }
                catch(IOException e)
                {
                    System.out.println("NET: ServerSocket failed to bind to port " + SERVER_PORT);
                    e.printStackTrace();
                    return; // non-recoverable error so terminate the thread
                }
            }
            else
            {
                // if hostAddress is set, attempt to connect. Otherwise wait for it to be set.
                if(getHostAddress() != null && !clientSocket.isConnected())
                {
                    try
                    {
                        clientSocket.bind(null);
                        clientSocket.connect(new InetSocketAddress(hostAddress, SERVER_PORT), 500);
                    }
                    catch(SocketTimeoutException et)
                    {
                        System.out.println("NET: Client timed out trying to connect to host. Retrying...");
                    }
                    catch(IOException e)
                    {
                        System.out.println("NET: Exception during client connection. Not retrying.");
                        e.printStackTrace();
                        return; // non-recoverable error so terminate the thread
                    }
                }
            }

            // if it's made it this far, everything should be ready.
            ready = true;
        }
    }
}
