package com.mygdx.game.desktop.net;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.net.NetworkInterface;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by NeilMoore on 2/25/2015.
 */
public class PCLocalNetwork implements NetworkInterface
{

    /**
     * Sets up the network infrastructure as needed.
     * @param client
     * @param server
     */
    @Override
    public java.util.ArrayList<java.net.InetAddress> setup(Client client, Server server)
    {
        System.out.println("Thread ID: " + Thread.currentThread().getId());
        System.out.println("NET: Network interface setting up!");
        if(client != null)
        {
            List<InetAddress> addresses = client.discoverHosts(NetworkManager.SERVER_UDP_PORT, 5000);
            if(addresses != null && addresses instanceof ArrayList)
            {
                return (ArrayList<InetAddress>)addresses;
            }
        }

        if(server != null)
        {
            // other server-specific stuff goes here.
            return null;
        }

        return null;
    }

    /**
     * Returns if the interface is ready to be used, i.e. connected to the Internet
     * and sockets can be made.
     *
     * @return true if ready, false if not
     */
    @Override
    public boolean isReady()
    {
        boolean ready = false;
        try
        {
            Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements() && !ready)
            {
                java.net.NetworkInterface networkInterface = interfaces.nextElement();
                if(networkInterface.isUp() && !networkInterface.isLoopback())
                {
                    System.out.println("NET: Interface " + networkInterface.getName() + " is up!");
                    ready = true;
                }
            }
        }
        catch(SocketException e)
        {
            System.out.println("NET: SocketException thrown while getting network interfaces.");
            e.printStackTrace();
            ready = false;
        }

        return ready;
    }

    /**
     * Allows the network interface to update if needed.
     */
    @Override
    public void update()
    {
    }

    /**
     * Clean up resources used by the network interface
     */
    @Override
    public void cleanup()
    {

    }
}
