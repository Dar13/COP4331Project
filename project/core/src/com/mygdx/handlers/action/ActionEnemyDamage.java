package com.mygdx.handlers.action;

import com.mygdx.entities.Enemy;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by rob on 3/23/15.
 */
public class ActionEnemyDamage extends ActionEnemyBase
{
    public int damage;

    public ActionEnemyDamage(Enemy enemy, int damage)
    {
        super(enemy);

        actionClass = ActionClass.ACTION_ENEMY_DAMAGE;

        this.damage = damage;
    }
}
