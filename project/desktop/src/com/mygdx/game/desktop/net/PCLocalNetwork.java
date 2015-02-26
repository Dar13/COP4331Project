package com.mygdx.game.desktop.net;

import com.mygdx.net.NetworkInterface;

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

    }

    /**
     * Returns if the interface is ready to be used.
     *
     * @return true if ready, false if not
     */
    @Override
    public boolean isReady()
    {
        return false;
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
