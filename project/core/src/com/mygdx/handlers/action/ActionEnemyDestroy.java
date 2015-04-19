package com.mygdx.handlers.action;

import com.mygdx.entities.Enemy;

/**
 * Created by rob on 3/25/15.
 */
public class ActionEnemyDestroy extends ActionEnemyBase
{
    public ActionEnemyDestroy()
    {

    }

    public ActionEnemyDestroy(Enemy enemy)
    {
        super(enemy);

        actionClass = ActionClass.ACTION_ENEMY_DESTROY;
    }
}

