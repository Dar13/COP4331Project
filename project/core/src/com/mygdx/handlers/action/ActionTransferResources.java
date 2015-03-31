package com.mygdx.handlers.action;

import com.mygdx.handlers.NetworkManager;
import com.mygdx.states.GameState;

/**
 * Created by rob on 3/25/15.
 */
public class ActionTransferResources extends Action
{
    public int gold, playerFrom, playerTo;

    public ActionTransferResources(NetworkManager networkManager, int gold, int playerFrom, int playerTo)
    {
        super(networkManager);

        this.networkManager = networkManager;

        actionClass = ActionClass.ACTION_TRANSFER_RESOURCES;

        this.gold = gold;
        this.playerFrom = playerFrom;
        this.playerTo = playerTo;

        updateNetMan();
    }

    @Override
    public void updateNetMan()
    {
        networkManager.addToSendQueue(this);
    }
}
