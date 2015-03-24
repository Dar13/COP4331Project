package com.mygdx.handlers.action;

import com.NewEntities.NewEnemy;
import com.mygdx.entities.Enemy;

/**
 * Created by rob on 3/23/15.
 */
public class ActionEnemyCreate extends Action
{
    public int tempID;

    public ActionEnemyCreate(NewEnemy newEnemy)
    {
        actionClass = ActionClass.ACTION_ENEMY_CREATE;
        this.entity = newEnemy;

        if(localChange)
        {
            newEnemy.entityID = tempEntityID.getAndIncrement();
            needsID = true;
        }

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
