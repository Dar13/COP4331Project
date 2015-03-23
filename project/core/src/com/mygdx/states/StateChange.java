package com.mygdx.states;

import java.util.List;

/**
 * Created by rob on 3/5/2015.
 * This object is what will be shared and compared for communication between client and server
 */
public class StateChange
{

    /**
     * The following is a (non-exhaustive) list of potential state changes. Use these in order to
     * clarify which change is specified elsewhere in the code.
     * EX: When creating a tower you create a new StateChange with the following:
     *      List<Object> params = new ArrayList<Object>();
     *      params.add(tower.X);
     *      params.add(tower.Y);
     *      params.add(tower.type);
     *      StateChange sc = new StateChange(0, ChangeType.CHANGE_TYPE_CREATE_TOWER, params);
     */
    public static final int CHANGE_TYPE_CREATE_TOWER = 0;
    public static final int CHANGE_TYPE_SPAWN_ENEMY = 1;
    public static final int CHANGE_TYPE_ALTER_PATH = 2;
    public static final int CHANGE_TYPE_ATTACK = 3;
    public static final int CHANGE_TYPE_ALTER_HEALTH = 4;
    public static final int CHANGE_TYPE_DIE = 5;

    /**
     * This variable is used to create a temporary variable until the server can assign a global,
     * authoritative ID
     */
    private static int dummyID = 0;

    private int entityID; //all changes must be linked to an entity, referenced by an int ID
    private int changeType; //reference to the change class
    private List parameters; //used Object to make this as general as possible

    public StateChange(int entityID, int changeType, List parameters)
    {
        //If this is a new entity, assign a temporary ID, otherwise, use the given one
        if(changeType == CHANGE_TYPE_CREATE_TOWER || changeType == CHANGE_TYPE_SPAWN_ENEMY)
            this.entityID = dummyID++;
        else
            this.entityID = entityID;

        this.changeType = changeType;
        this.parameters = parameters;
    }

    public int getEntityID()
    {
        return entityID;
    }

    public int getChangeType()
    {
        return changeType;
    }

    public Object getParameters()
    {
        return parameters;
    }
}
