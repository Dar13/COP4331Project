package com.mygdx.handlers.action;

import com.mygdx.entities.Entity;
import com.mygdx.handlers.NetworkManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class should never be used directly EXCEPT to obtain action class IDs. To define a new
 * action class, declare it in the enum, and then create a class which extends Action to define its
 * specifics.
 *
 * Subclasses should be parametrized with a GameState and NetworkManager object FIRST, followed by
 * any relevant parameters to reconstruct the Action on the other side.
 *
 * The updateNetMan() function currently only contains one method call, which is made
 * in each subclass. This will likely change, but I left it that way in case we later change the
 * way NetMan is updated about new Actions.
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
        ACTION_TRANSFER_RESOURCES,
        ACTION_HOST_PAUSE
    }

    //This bool differentiates between locally generated actions and actions received from server
    public boolean localChange, needsID;
    public ActionClass actionClass; //not sure if this will be needed -- would rather have it
    public Entity entity = null;

    public static AtomicInteger tempEntityID = new AtomicInteger(0);
}
