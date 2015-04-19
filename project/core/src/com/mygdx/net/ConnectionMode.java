package com.mygdx.net;

/**
* Created by NeilMoore on 4/3/2015.
*/
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
        switch (idx)
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
