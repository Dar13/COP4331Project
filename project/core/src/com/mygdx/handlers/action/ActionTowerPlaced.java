package com.mygdx.handlers.action;

import com.mygdx.entities.Tower;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.states.GameState;

/**
 * Created by rob on 3/25/15.
 */
public class ActionTowerPlaced extends Action
{
    public ActionTowerPlaced(GameState gameState, NetworkManager networkManager, Tower tower)
    {
        super(networkManager);

        this.networkManager = networkManager;

        actionClass = Action.ActionClass.ACTION_TOWER_PLACED;
        this.entity = tower;

        tower.entityID = tempEntityID.getAndIncrement();
        needsID = true;

        updateNetMan();
    }

    @Override
    public void updateNetMan()
    {
        networkManager.addToSendQueue(this);
    }
}
