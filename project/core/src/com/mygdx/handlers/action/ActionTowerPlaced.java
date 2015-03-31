package com.mygdx.handlers.action;

import com.mygdx.entities.Tower;

/**
 * Created by rob on 3/25/15.
 */
public class ActionTowerPlaced extends Action
{
    public ActionTowerPlaced(Tower tower)
    {
        actionClass = Action.ActionClass.ACTION_TOWER_PLACED;
        this.entity = tower;

        tower.entityID = tempEntityID.getAndIncrement();
        needsID = true;
    }
}
