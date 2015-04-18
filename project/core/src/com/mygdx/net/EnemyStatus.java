package com.mygdx.net;

import com.mygdx.entities.Enemy;
import com.mygdx.entities.Entity;
import com.mygdx.handlers.action.ActionEnemyBase;
import com.mygdx.handlers.action.ActionEnemyCreate;

/**
 * Created by NeilMoore on 4/3/2015.
 */
public class EnemyStatus extends EntityStatus
{
    public Enemy.Type enemyType;
    public float health;
    public float armor;
    public float velocity;

    public EnemyStatus(ActionEnemyBase action)
    {
        enemyType = action.enemyType;
        region = action.region;

        if(action instanceof ActionEnemyCreate)
        {
            ActionEnemyCreate create = (ActionEnemyCreate)action;
            health = create.health;
            armor = create.armor;
        }

        // other types
    }
}
