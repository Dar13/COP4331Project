package com.mygdx.handlers.action;

import com.mygdx.entities.Enemy;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.net.EnemyStatus;
import com.mygdx.states.GameState;

/**
 * Created by rob on 3/23/15.
 */
public class ActionEnemyCreate extends ActionEnemyBase
{
    public float health;
    public float armor;
    public float velocity;

    public ActionEnemyCreate()
    {
        super();
    }

    public ActionEnemyCreate(Enemy newEnemy)
    {
        super(newEnemy);

        this.needsID = needsID;

        actionClass = ActionClass.ACTION_ENEMY_CREATE;

        velocity = newEnemy.getVelocity();
        health = newEnemy.getHealth();
        armor = newEnemy.getArmor();
    }

    public ActionEnemyCreate(EnemyStatus enemyStatus)
    {
        super(enemyStatus);

        actionClass = ActionClass.ACTION_ENEMY_CREATE;

        health = enemyStatus.health;
        armor = enemyStatus.armor;
        velocity = enemyStatus.velocity;
    }
}
