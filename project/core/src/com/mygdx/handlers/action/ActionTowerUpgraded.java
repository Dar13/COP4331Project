package com.mygdx.handlers.action;

import com.NewEntities.Tower;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.states.GameState;

/**
 * Created by rob on 3/25/15.
 */
public class ActionTowerUpgraded extends Action
{
    public ActionTowerUpgraded(GameState gameState, NetworkManager networkManager, Tower tower)
    {
        super(gameState, networkManager);

        this.networkManager = networkManager;
        this.gameState = gameState;

        actionClass = Action.ActionClass.ACTION_TOWER_UPGRADED;
        this.entity = tower;

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
