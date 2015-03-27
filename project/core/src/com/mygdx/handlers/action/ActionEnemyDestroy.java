package com.mygdx.handlers.action;

import com.NewEntities.Enemy;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.states.GameState;

/**
 * Created by rob on 3/25/15.
 */
public class ActionEnemyDestroy extends Action
{
    public ActionEnemyDestroy(GameState gameState, NetworkManager networkManager, Enemy enemy)
    {
        super(gameState, networkManager);

        this.networkManager = networkManager;
        this.gameState = gameState;

        actionClass = ActionClass.ACTION_ENEMY_DESTROY;
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

