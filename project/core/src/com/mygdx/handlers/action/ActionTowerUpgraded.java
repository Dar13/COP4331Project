package com.mygdx.handlers.action;

import com.mygdx.entities.Tower;

/**
 * Created by rob on 3/25/15.
 */
public class ActionTowerUpgraded extends ActionTowerBase
{
    public ActionTowerUpgraded(Tower tower)
    {
        super(tower);

        actionClass = ActionClass.ACTION_TOWER_UPGRADED;
    }
}
