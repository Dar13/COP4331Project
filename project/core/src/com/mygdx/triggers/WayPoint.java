package com.mygdx.triggers;

/**
 * Created by James on 2/14/2015.
 */
public class WayPoint
{
    public float x;
    public float y;
    public String direction; // NOTE: change to use enum rather than string. More efficient that way

    public WayPoint(float x, float y, String direction)
    {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

}
