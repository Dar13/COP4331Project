package com.mygdx.handlers.action;

import com.mygdx.entities.Enemy;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by rob on 3/23/15.
 */
public class ActionEnemyDamage extends Action
{
    private int damage;

    public ActionEnemyDamage(NetworkManager networkManager, Enemy enemy, int damage)
    {
        super(networkManager);

        this.networkManager = networkManager;

        actionClass = ActionClass.ACTION_ENEMY_DAMAGE;
        this.entity = enemy;

        updateGamestate();
        updateNetMan();
    }

    @Override
    public void updateGamestate()
    {
        /**
         * TODO -- add gameState changes
         */
    }

    @Override
    public void updateNetMan()
    {
        networkManager.addToSendQueue(this);
    }
}
