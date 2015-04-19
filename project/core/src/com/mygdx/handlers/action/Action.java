package com.mygdx.handlers.action;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class should never be used directly EXCEPT to obtain action class IDs. To define a new
 * action class, declare it in the enum, and then create a class which extends Action to define its
 * specifics.
 */
public abstract class Action
{
    protected static AtomicInteger uniqueIDCounter = new AtomicInteger(1);

    public enum ActionClass {
        ACTION_ENEMY_CREATE,
        ACTION_ENEMY_DAMAGE,
        ACTION_ENEMY_DESTROY,
        ACTION_ENEMY_END,
        ACTION_TOWER_PLACED,
        ACTION_TOWER_UPGRADED,
        ACTION_TRANSFER_RESOURCES,
        ACTION_HOST_PAUSE,
        ACTION_WAIT_FOR_READY,
        ACTION_HEALTH_CHANGED,
        ACTION_PLAYER_READY,
        ACTION_CREATE_WAVE,
        ACTION_PLAYER_WAVE_READY,
        ACTION_WAVE_START,
        ACTION_WAVE_END
    }

    public Action()
    {
        uniqueID = uniqueIDCounter.getAndIncrement();
    }

    //This bool differentiates between locally generated actions and actions received from server
    public boolean localChange;
    public boolean needsID;
    public int region;
    public ActionClass actionClass; //not sure if this will be needed -- would rather have it
    public int uniqueID = 0; // UID being 0 represents an action sent by the server/singleplayer to itself
}
