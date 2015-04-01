package com.mygdx.handlers.action;

import com.mygdx.entities.Entity;
import com.mygdx.handlers.NetworkManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class should never be used directly EXCEPT to obtain action class IDs. To define a new
 * action class, declare it in the enum, and then create a class which extends Action to define its
 * specifics.
 */
public abstract class Action
{
    public enum ActionClass {
        ACTION_ENEMY_CREATE,
        ACTION_ENEMY_DAMAGE,
        ACTION_ENEMY_DESTROY,
        ACTION_ENEMY_END,
        ACTION_TOWER_PLACED,
        ACTION_TOWER_UPGRADED,
        ACTION_TRANSFER_RESOURCES,
        ACTION_HOST_PAUSE,
        ACTION_HEALTH_CHANGED,
    }

    //This bool differentiates between locally generated actions and actions received from server
    public boolean localChange, needsID;
    public ActionClass actionClass; //not sure if this will be needed -- would rather have it
    public Entity entity = null;
    public int UID = 0; // UID being 0 represents an action sent by the server/singleplayer to itself

    public static AtomicInteger tempEntityID = new AtomicInteger(0);
}
