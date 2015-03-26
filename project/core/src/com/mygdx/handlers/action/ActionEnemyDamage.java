package com.mygdx.handlers.action;

import com.NewEntities.NewEnemy;
import com.badlogic.gdx.Net;
import com.mygdx.entities.Enemy;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.states.GameState;

/**
 * Created by rob on 3/23/15.
 */
public class ActionEnemyDamage extends Action
{
    private int damage;

    public ActionEnemyDamage(GameState gameState, NetworkManager networkManager, NewEnemy enemy, int damage)
    {
        super(gameState, networkManager);

        this.networkManager = networkManager;
        this.gameState = gameState;

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
