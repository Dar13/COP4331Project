package com.mygdx.triggers;

/**
 * Created by James on 2/14/2015.
 */
public class WayPoint {
    public float x;
    public float y;
    public String direction;
    public boolean isSpawn;

    public  WayPoint(float x, float y, String direction, boolean isSpawn){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.isSpawn = isSpawn;
    }

}
