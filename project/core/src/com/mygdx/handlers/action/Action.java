package com.mygdx.handlers.action;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class should never be used directly EXCEPT to obtain action class IDs. To define a new
 * action class, declare it in the enum, and then create a class which extends Action to define its
 * specifics
 */
public abstract class Action
{
    public enum ActionClass {
        ACTION_ENEMY_CREATE,
        ACTION_ENEMY_DAMAGE,
        ACTION_ENEMY_DESTROY,
        ACTION_ENEMY_WIN,
        ACTION_TOWER_PLACED,
        ACTION_TOWER_UPGRADED,
        ACTION_PLAYER_SEND_RESOURCES,
        ACTION_PLAYER_RECEIVE_RESOURCES,
        ACTION_HOST_PAUSE
    }

    //This bool differentiates between locally generated actions and actions received from server
    public boolean localChange, needsID;
    public ActionClass actionClass; //not sure if this will be needed -- would rather have it
    public Entity entity = null;

    public static AtomicInteger tempEntityID = new AtomicInteger(0);

    public Action()
    {
        localChange = true;
        needsID = false;
    }

    //These methods will contain the implementation specific code for each type of action
    public abstract void updateGamestate();
    public abstract void updateNetMan();
}
