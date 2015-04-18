package com.mygdx.handlers.action;

import com.mygdx.entities.Enemy;

/**
 * Created by rob on 3/25/15.
 */
public class ActionEnemyEnd extends ActionEnemyBase
{
    public ActionEnemyEnd()
    {

    }
    /**
     * This action is created when an enemy reaches the end of the client screen.
     * The server either moves it to another screen, or decrements health accordingly.
     * @param enemy
     */
    public ActionEnemyEnd(Enemy enemy)
    {
        super(enemy);

        actionClass = ActionClass.ACTION_ENEMY_END;
    }
}
