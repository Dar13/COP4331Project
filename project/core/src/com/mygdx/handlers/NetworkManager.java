package com.mygdx.handlers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Abstraction away from low-level networking for use in the game states.
 */
public class NetworkManager implements Runnable
{
    public static final int SERVER_PORT = 0xDDD;

    enum ConnectionMode
    {
        WIFI_P2P,
        WIFI_LAN,
        DATA_4G,
        NONE
    }

    protected Boolean initialized;

    // server
    protected Boolean isServer;
    protected ServerSocket serverSocket;
    protected ArrayList<Socket> connections;

    // client socket
    protected Socket clientSocket;

    // techniques of connecting
    protected ConnectionMode preferredMode;
    protected ConnectionMode fallbackMode;


    public NetworkManager()
    {
        initialized = initialize();
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

        if(isServer != null)
        {
            if (isServer)
            {
                try
                {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                clientSocket = new Socket();
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    public boolean isInitialized()
    {
        return (initialized == null) ? false : initialized;
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

    }
}
