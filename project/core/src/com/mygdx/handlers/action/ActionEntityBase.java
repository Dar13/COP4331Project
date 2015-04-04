package com.mygdx.handlers.action;

import com.mygdx.entities.Entity;
import com.mygdx.net.EntityStatus;

/**
 * Created by NeilMoore on 4/3/2015.
 */
public class ActionEntityBase extends Action
{
    public int entityID;
    public int tempID;

    public ActionEntityBase(Entity entity)
    {
        super();

        entityID = entity.entityID;
        tempID = entity.tempID;

        needsID = true;
    }

    public ActionEntityBase(EntityStatus entityStatus)
    {
        super();

        entityID = entityStatus.uniqueID;
        needsID = false;
    }
}
