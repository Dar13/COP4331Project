package com.mygdx.net;

import com.mygdx.handlers.action.ActionTowerBase;
import com.mygdx.handlers.action.ActionTowerPlaced;

/**
 * Created by NeilMoore on 4/3/2015.
 */
public class TowerStatus extends EntityStatus
{
    public Class<?> towerType;
    public float range;
    public float damage;
    public int level;

    public TowerStatus(ActionTowerBase action)
    {
        towerType = action.towerType;
        region = action.region;

        if(action instanceof ActionTowerPlaced)
        {
            ActionTowerPlaced towerPlaced = (ActionTowerPlaced)action;
            level = towerPlaced.level;
        }

        
    }
}
