package com.mygdx.game.android.net;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.handlers.net.NetworkManager;
import com.mygdx.net.NetworkInterface;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeilMoore on 2/25/2015.
 */
public class AndroidLocalNetwork implements NetworkInterface
{
    protected Context appContext;
    protected String wifiIP;
    protected String wifiLanIP;

    public AndroidLocalNetwork(Context context)
    {
        appContext = context;

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddr = wifiManager.getConnectionInfo().getIpAddress();

        if(ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN))
        {
            ipAddr = Integer.reverseBytes(ipAddr);
        }

        byte[] ipByte = BigInteger.valueOf(ipAddr).toByteArray();

        try
        {
            wifiIP = InetAddress.getByAddress(ipByte).getHostAddress();
        }
        catch(UnknownHostException e)
        {
            System.out.println("NET: Unable to get host address");
            wifiIP = "<null>";
        }

    }

    /**
     * Sets up the network infrastructure as needed.
     * @param client
     * @param server
     */
    @Override
    public java.util.ArrayList<java.net.InetAddress> setup(Client client, Server server)
    {
        System.out.println("NET: Wifi Host IP = " + wifiIP);
        // Initialize Wifi stuff for Android.
        if(client != null)
        {
            // discover hosts and return
            List<InetAddress> addresses = client.discoverHosts(NetworkManager.SERVER_UDP_PORT, 5000);
            if(addresses instanceof ArrayList)
            {
                return (ArrayList<InetAddress>)addresses;
            }
        }

        if(server != null)
        {
            return null;
        }

        return null;
    }

    /**
     * Returns if the interface is ready to be used.
     *
     * @return true if ready, false if not
     */
    @Override
    public boolean isReady()
    {
        return true;
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
