package com.mygdx.triggers;

/**
 * Created by James on 2/14/2015.
 */
public class WayPoint
{
    public float x;
    public float y;
    public enum Direction
    {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        END, // NOTE: Discussion is needed
        NONE
    }

    public Direction direction;

    public WayPoint(float x, float y, Direction direction)
    {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

}
