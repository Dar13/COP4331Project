package com.mygdx.handlers.action;

import com.mygdx.entities.Enemy;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.states.GameState;

/**
 * Created by rob on 3/25/15.
 */
public class ActionEnemyDestroy extends Action
{
    public ActionEnemyDestroy(Enemy enemy)
    {
        actionClass = ActionClass.ACTION_ENEMY_DESTROY;
        this.entity = enemy;
    }
}

