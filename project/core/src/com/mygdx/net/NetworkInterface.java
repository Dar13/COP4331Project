package com.mygdx.net;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

/**
 * Interface for all internal network implementations.
 */
public interface NetworkInterface
{
    /**
     * Sets up the network infrastructure as needed.
     * @param client
     * @param server
     */
    public java.util.ArrayList<java.net.InetAddress> setup(Client client, Server server);

    /**
     * Returns if the interface is ready to be used.
     *
     * @return true if ready, false if not
     */
    public boolean isReady();

    /**
     * Allows the network interface to update if needed.
     */
    public void update();

    /**
     * Clean up resources used by the network interface
     */
    public void cleanup();
}
