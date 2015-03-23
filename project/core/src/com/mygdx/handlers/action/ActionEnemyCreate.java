package com.mygdx.handlers.action;

import com.mygdx.entities.Enemy;

/**
 * Created by rob on 3/23/15.
 */
public class ActionEnemyCreate extends Action
{
    public int tempID;
    public Enemy newEnemy;

    public ActionEnemyCreate(Enemy newEnemy)
    {
        actionClass = ActionClass.ACTION_ENEMY_CREATE;
        this.newEnemy = newEnemy;

        if(localChange)
            newEnemy.entityID = tempEntityID.getAndIncrement();


    }

    @Override
    public void updateGamestate()
    {
    }

    @Override
    public void updateNetMan()
    {
    }
}
