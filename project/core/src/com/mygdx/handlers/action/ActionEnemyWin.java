package com.mygdx.handlers.action;

import com.NewEntities.NewEnemy;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.states.GameState;

/**
 * Created by rob on 3/25/15.
 */
public class ActionEnemyWin extends Action
{
    public ActionEnemyWin(GameState gameState, NetworkManager networkManager, NewEnemy enemy)
    {
        super(gameState, networkManager);

        this.networkManager = networkManager;
        this.gameState = gameState;

        actionClass = ActionClass.ACTION_ENEMY_WIN;
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
