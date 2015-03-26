package com.mygdx.handlers.action;

import com.NewEntities.Enemy;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.states.GameState;

/**
 * Created by rob on 3/23/15.
 */
public class ActionEnemyCreate extends Action
{
    public int tempID;

    public ActionEnemyCreate(GameState gameState, NetworkManager networkManager, Enemy newEnemy)
    {
        super(gameState, networkManager);

        actionClass = ActionClass.ACTION_ENEMY_CREATE;
        this.entity = newEnemy;

        newEnemy.entityID = tempEntityID.getAndIncrement();
        needsID = true;

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
