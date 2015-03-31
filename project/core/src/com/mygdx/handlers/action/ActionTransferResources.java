package com.mygdx.handlers.action;

import com.mygdx.handlers.NetworkManager;
import com.mygdx.states.GameState;

/**
 * Created by rob on 3/25/15.
 */
public class ActionTransferResources extends Action
{
    public int gold, playerFrom, playerTo;

    public ActionTransferResources(int gold, int playerFrom, int playerTo)
    {
        actionClass = ActionClass.ACTION_TRANSFER_RESOURCES;

        this.gold = gold;
        this.playerFrom = playerFrom;
        this.playerTo = playerTo;
    }
}
