package com.mygdx.handlers.action;

import com.mygdx.entities.Enemy;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.states.GameState;

/**
 * Created by rob on 3/23/15.
 */
public class ActionEnemyCreate extends Action
{
    public int tempID;

    public ActionEnemyCreate(Enemy newEnemy)
    {
        actionClass = ActionClass.ACTION_ENEMY_CREATE;
        this.entity = newEnemy;

        newEnemy.entityID = tempEntityID.getAndIncrement();
        needsID = true;
    }
}
