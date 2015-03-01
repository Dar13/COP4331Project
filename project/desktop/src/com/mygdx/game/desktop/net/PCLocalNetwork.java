package com.mygdx.game.desktop.net;

import com.mygdx.net.NetworkInterface;

import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by NeilMoore on 2/25/2015.
 */
public class PCLocalNetwork implements NetworkInterface
{

    /**
     * Sets up the network infrastructure as needed.
     */
    @Override
    public void setup()
    {
        // pretty default PC networking setup. Not sure what to do here.
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
            while(interfaces.hasMoreElements())
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
