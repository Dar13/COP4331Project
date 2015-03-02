package com.mygdx.game.android.net;

import android.content.Context;

import com.mygdx.net.NetworkInterface;

/**
 * Created by NeilMoore on 2/25/2015.
 */
public class AndroidLocalNetwork implements NetworkInterface
{
    protected Context appContext;

    public AndroidLocalNetwork(Context context)
    {
        appContext = context;
    }

    /**
     * Sets up the network infrastructure as needed.
     */
    @Override
    public void setup()
    {
        // Initialize Wifi stuff for Android.
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
