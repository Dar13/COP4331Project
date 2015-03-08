package com.mygdx.net;

import com.esotericsoftware.kryonet.Connection;

/**
 * Created by NeilMoore on 3/4/2015.
 */
public class GameConnection extends Connection
{
    public static class ServerAuth
    {
        public int key = 0xBEEF; // little bit of security through obscurity
    }

    public static class ClientAuth
    {
        public int key = 0xDEAD; // little bit of security through obscurity
    }

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
