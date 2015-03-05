package com.mygdx.net;

import com.esotericsoftware.kryonet.Connection;

/**
 * Created by NeilMoore on 3/4/2015.
 */
public class GameConnection extends Connection
{
    protected int uid;
    protected boolean validated;

    public int getUID()
    {
        return uid;
    }

    public void assignUID(int uid)
    {
        this.uid = uid;
        validated = true;
    }

    public boolean isValidated()
    {
        return validated;
    }
}
