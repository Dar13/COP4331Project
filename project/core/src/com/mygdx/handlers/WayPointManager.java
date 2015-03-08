package com.mygdx.handlers;

import com.mygdx.triggers.Path;
import com.mygdx.triggers.WayPoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * Created by LordNeah on 3/6/2015.
 */
public class WayPointManager
{

    public LinkedList<WayPoint> wayPoints;
    public LinkedList<Path> paths;
    public File inputtxt;

    public WayPointManager()
    {
        inputtxt = new File("Waypoints.txt");
        this.wayPoints = new LinkedList<WayPoint>();
        this.paths = new LinkedList<Path>();
        try
        {
            ReadInWaypoints();
        }
        catch (FileNotFoundException e)
        {
        }

        ConstructPaths();
    }

    private void ReadInWaypoints() throws FileNotFoundException
    {
        Scanner in = new Scanner(inputtxt);
        in.useDelimiter(" ");
        int xx = 0;
        int yy = 0;
        String dir = new String();
        while (in.hasNext())
        {
            if (in.hasNextInt())
            {
                int x = in.nextInt();
                xx = x;
            }

            if (in.hasNextInt())
            {
                int y = in.nextInt();
                yy = y;
            }

            if (in.hasNext())
            {
                int direction = in.nextInt();
                if (direction == 1)
                {
                    wayPoints.addLast(new WayPoint(xx, yy, WayPoint.Direction.NORTH));
                }
                else if (direction == 2)
                {
                    wayPoints.addLast(new WayPoint(xx, yy, WayPoint.Direction.SOUTH));
                }
                else if (direction == 3)
                {
                    wayPoints.addLast(new WayPoint(xx, yy, WayPoint.Direction.EAST));
                }
                else if (direction == 4)
                {
                    wayPoints.addLast(new WayPoint(xx, yy, WayPoint.Direction.WEST));
                }
                else if (direction == 5)
                {
                    wayPoints.addLast(new WayPoint(xx, yy, WayPoint.Direction.END));
                }
            }

        }

        in.close();
    }

    private void ConstructPaths()
    {
        int i = 0;
        while(i < wayPoints.size() - 1)
        {
            if (wayPoints.get(i).x == wayPoints.get(i+1).x)
            {
                if(wayPoints.get(i).y > wayPoints.get(i+1).y)
                {
                    Path NEW = new Path(wayPoints.get(i+1).x, wayPoints.get(i+1).y, wayPoints.get(i).x + 32, wayPoints.get(i).y + 32);
                }
                else
                {
                    Path NEW = new Path(wayPoints.get(i).x, wayPoints.get(i).y, wayPoints.get(i+1).x + 32, wayPoints.get(i+1).y + 32);
                }
            }

            else if (wayPoints.get(i).y == wayPoints.get(i+1).y)
            {
                if(wayPoints.get(i).x > wayPoints.get(i+1).x)
                {
                    Path NEW = new Path(wayPoints.get(i+1).x, wayPoints.get(i+1).y, wayPoints.get(i).x + 32, wayPoints.get(i).y + 32);
                }
                else
                {
                    Path NEW = new Path(wayPoints.get(i).x, wayPoints.get(i).y, wayPoints.get(i+1).x + 32, wayPoints.get(i+1).y + 32);
                }
            }
            i++;
        }
    }

    public boolean WithinAny(float x, float y){
        int i = 0;

        while (i < paths.size()){
            if (paths.get(i).IsWithin(x, y)){
                return true;
            }
        }

        return false;
    }

}
