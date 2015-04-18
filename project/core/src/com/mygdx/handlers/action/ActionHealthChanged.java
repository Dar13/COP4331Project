package com.mygdx.handlers.action;

/**
 * Created by klang on 3/31/15.
 */
public class ActionHealthChanged extends Action
{
    public int newHealth;

    public ActionHealthChanged()
    {

    }

    public ActionHealthChanged(int curHealth) {
        actionClass = ActionClass.ACTION_HEALTH_CHANGED;
        newHealth = curHealth;
    }
}
