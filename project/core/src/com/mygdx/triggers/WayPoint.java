package com.mygdx.triggers;

import java.util.Random;

/**
 * Created by James on 2/14/2015.
 */
public class WayPoint
{
    public float x;
    public float y;
    private int path1;
    private int path2;
    private int path3;
    private int path4;
    public enum Direction
    {
        NORTH(0,1),
        SOUTH(0,-1),
        EAST(1,0),
        WEST(-1,0),
        END(0,0), // NOTE: Discussion is needed
        RAND(1,1),
        NONE(0,0);

        public int x,y;
        Direction(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    public Direction direction;

    public WayPoint(float x, float y, Direction direction)
    {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void randWaypointInfo(int path1, int path2, int path3, int path4){
        this.path1 = path1;
        this.path2 = path2;
        this.path3 = path3;
        this.path4 = path4;
    }

    public int returnRandPath(){
        Random rand = new Random();
        switch (rand.nextInt(4)){
            case 0:
                System.out.println(0);
                return path1;
            case 1:
                System.out.println(1);
                return path2;
            case 2:
                System.out.println(2);
                return path3;
            case 3:
                System.out.println(3);
                return path4;
        }
        return path1;
    }

}
