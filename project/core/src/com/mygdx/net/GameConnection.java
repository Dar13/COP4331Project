package com.mygdx.net;

import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.MyGame;

/**
 * Created by NeilMoore on 3/4/2015.
 */
public class GameConnection
{
    public static class ServerAuth
    {

        public int key = MyGame.VERSION;
    }

    public static class ClientAuth
    {
        public int key = MyGame.VERSION;
    }

    public Connection connection;

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
