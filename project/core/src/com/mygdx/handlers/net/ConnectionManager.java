package com.mygdx.handlers.net;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.handlers.ThreadSafeList;
import com.mygdx.handlers.action.Action;
import com.mygdx.net.ConnectionMode;
import com.mygdx.net.GameConnection;
import com.mygdx.net.NetworkInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rob on 4/19/15.
 */
public class ConnectionManager extends Listener
{
    // techniques of connecting
    protected ConnectionMode preferredMode;
    protected ConnectionMode fallbackMode;
    protected HashMap<ConnectionMode, NetworkInterface> networkInterfaces;
    protected NetworkInterface networkInterface;
    protected ThreadSafeList connections;
    protected ArrayList<InetAddress> serverAddresses;

    protected int expectedNumClients;
    protected boolean isServer, initializeNow, initialized;

    protected Client client;
    protected Server server;
    protected NetworkManager parent;

    protected InetAddress hostAddress;

    public ConnectionManager(HashMap<ConnectionMode, NetworkInterface> modes, int maxClients, NetworkManager nm)
    {
        networkInterfaces = modes;
        parent = nm;
        expectedNumClients = maxClients;
        isServer = false;
        initializeNow = false;
    }

    public boolean prepare(boolean isServer, ConnectionMode primary, ConnectionMode fallbackMode, boolean initializeNow)
    {
        this.isServer = isServer;
        preferredMode = primary;
        this.fallbackMode = fallbackMode;
        this.initializeNow = initializeNow;
        return true;
    }

    public boolean initConnection(Client c, Server s)
    {
        client = c;
        server = s;

        if (this.preferredMode == null)
        {
            initialized = false;
            return false;
        }

        networkInterface = networkInterfaces.get(this.preferredMode);

        if (networkInterface != null)
        {
            serverAddresses = networkInterface.setup(client, server);

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
                    serverAddresses = networkInterface.setup(client, server);
                    if (!networkInterface.isReady())
                    {
                        // fallback failed
                        System.out.println("NET: Fallback mode failed to initialize");
                        initialized = false;
                        return false;
                    }
                }
            }
        }

        else
        {
            System.out.println("NET: Selected connection mode is not supported.");
            initialized = false;
            return false;
        }

        return true;
    }

    public Client setupClient(Client client)
    {
        // if hostAddress isn't set and servers aren't found,
        // return false to force it to wait until it's set.
        if(getHostAddress() == null && serverAddresses == null)
        {
            return null;
        }

        // if the client isn't connected, attempt to connect. Will repeat infinitely at the moment.
        if (!client.isConnected())
        {
            try
            {
                client.start();
                if(getHostAddress() != null)
                {
                    client.connect(5000, hostAddress, NetworkManager.SERVER_TCP_PORT, NetworkManager.SERVER_UDP_PORT);
                }

                if(!client.isConnected() && serverAddresses != null)
                {
                    for(InetAddress address : serverAddresses)
                    {
                        try
                        {
                            client.connect(5000, address, NetworkManager.SERVER_TCP_PORT, NetworkManager.SERVER_UDP_PORT);
                        }
                        catch(SocketTimeoutException e)
                        {
                            System.out.println("NET: Client timed out trying to connect to host.");
                            System.out.println("NET: Attempting next host.");
                        }
                    }
                }
            }
            catch (SocketTimeoutException et)
            {
                System.out.println("NET: Client timed out trying to connect to host. Retrying...");
                return null;
            }
            catch (IOException e)
            {
                System.out.println("NET: Exception during client connection. Not retrying. Maybe.");
                e.printStackTrace();
                return null; // non-recoverable error so terminate the thread
            }
        }

        return client;
    }

    @Override
    public void connected(Connection connection)
    {
        if(isServer)
        {
            if(connections.size() < expectedNumClients)
            {
                GameConnection gameConnection = new GameConnection();
                gameConnection.connection = connection;
                connections.add(gameConnection);
            }
            else
            {
                // don't need anymore connections. (rethink if we ever do chromecast stuff)
                connection.close();
                System.out.println("NET: Closing connection. ID = " + connection.getID());
            }
        }
        else
        {
            // send validation packet
            GameConnection.ClientAuth authPacket = new GameConnection.ClientAuth();
            client.sendTCP(authPacket);

            System.out.println("NET: Connected to server! Connection ID = " + connection.getID());
        }
    }

    @Override
    public void received(Connection connection, Object object)
    {
        //System.out.println("NET: Packet received!");
        boolean handled = false;

        if(isServer)
        {
            // Might want to change to hashmap<id, connection> and have the connection ID or some other UID
            // embedded in the packet itself. Not sure if its worth it, as the connections list should
            // fairly small (n < [4,8,12,16])
            for (GameConnection gameConnection : connections)
            {
                if (gameConnection.connection.getID() == connection.getID())
                {
                    handled = true;

                    if (!gameConnection.isValidated())
                    {
                        parent.handleValidation(gameConnection, null, object);
                        System.out.println("NET: TCP Packet received from Connection! ID = " + gameConnection.connection.getID());
                    }
                    else
                    {
                        // handle normally.
                        if(object instanceof Action)
                        {
                            Action action = (Action)object;
                            action.region = gameConnection.playerID;
                            parent.receiveChange(action);
                        }
                    }
                }
            }
        }

        else // client packet handling
        {
            handled = true;

            System.out.println("NET: Packet received from server!");
            System.out.println(object.getClass());
            if(!validated)
            {
                validated = handleValidation(null, connection, object);
            }

            if(object instanceof GameConnection.PlayerID)
            {
                playerID = ((GameConnection.PlayerID) object).playerID;
            }

            // assume authenticated, handle packet normally
            if(object instanceof Action)
            {
                queuedRemoteChanges.add((Action)object);
                System.out.println("NET: Packet is Action of type " + ((Action) object).actionClass);
            }
        }

        // mystery packet!
        if(!handled)
        {
            System.out.println("NET: Unknown packet received. Connection ID = " + connection.getID());
            System.out.println("NET: Unknown packet source = " + connection.getRemoteAddressTCP());
        }
    }

    public InetAddress getHostAddress()
    {
        return hostAddress;
    }

    public void setHostAddress(InetAddress address)
    {
        hostAddress = address;
    }

    public HashMap<ConnectionMode, NetworkInterface> getNetworkImpls()
    {
        return networkInterfaces;
    }
}
