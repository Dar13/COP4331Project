package com.mygdx.handlers.action;

import com.mygdx.entities.Enemy;
import com.mygdx.entities.Entity;
import com.mygdx.net.EnemyStatus;

/**
 * Created by NeilMoore on 4/3/2015.
 */
public class ActionEnemyBase extends ActionEntityBase
{
    public Enemy.Type enemyType;

    public ActionEnemyBase(Enemy enemy)
    {
        super(enemy);

        enemyType = enemy.type;
    }

    public ActionEnemyBase(EnemyStatus enemyStatus)
    {
        super(enemyStatus);

        enemyType = enemyStatus.enemyType;
    }
}
