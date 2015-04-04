package com.mygdx.handlers.action;

import com.mygdx.entities.Enemy;
import com.mygdx.net.EnemyStatus;

/**
 * Created by NeilMoore on 4/3/2015.
 */
public class ActionEnemyBase extends ActionEntityBase
{
    public Class<?> enemyType;

    public ActionEnemyBase(Enemy enemy)
    {
        super(enemy);

        enemyType = enemy.getClass();
    }

    public ActionEnemyBase(EnemyStatus enemyStatus)
    {
        super(enemyStatus);

        enemyType = enemyStatus.enemyType;
    }
}
