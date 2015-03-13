package com.mygdx.handlers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.MyGame;
import com.mygdx.triggers.Path;
import com.mygdx.triggers.WayPoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.FileHandler;


/**
 * Created by LordNeah on 3/6/2015.
 */
public class WayPointManager
{

    public LinkedList<WayPoint> wayPoints;
    public LinkedList<Path> paths;
    public File inputtxt;
    boolean inAndroid;
    //Hardcoded int array of waypoints for android version.
    private int[] map1 = new int[]{0, 0, 3, 608, 0, 1, 608, 448, 4, 0, 448, 2, 0, 0, 5};

    public WayPointManager(boolean inAndroid)
    {
        this.inAndroid = inAndroid;
        this.wayPoints = new LinkedList<WayPoint>();
        this.paths = new LinkedList<Path>();
        try
        {
            ReadInWaypoints();
        }
        catch (Exception e)
        {

        }

        ConstructPaths();
    }

    /*Reads in waypoints from a file if running in desktop, pulls them from the int array if
    running in android.
     */
    private void ReadInWaypoints() throws FileNotFoundException
    {
        if (!inAndroid)
        {
            FileHandle handle = Gdx.files.internal("Waypoints.txt");
            File file = handle.file();
            Scanner in = new Scanner(file);
            in.useDelimiter(" ");
            int xx = 0;
            int yy = 0;

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

                if (in.hasNextInt())
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

        else if(inAndroid)
        {
            for(int i = 0; i < map1.length; i ++)
            {
                int x = map1[i];
                i++;
                int y = map1[i];
                i++;
                int dir = map1[i];
                if (dir == 1)
                {
                    wayPoints.addLast(new WayPoint(x, y, WayPoint.Direction.NORTH));
                }
                else if (dir == 2)
                {
                    wayPoints.addLast(new WayPoint(x, y, WayPoint.Direction.SOUTH));
                }
                else if (dir == 3)
                {
                    wayPoints.addLast(new WayPoint(x, y, WayPoint.Direction.EAST));
                }
                else if (dir == 4)
                {
                    wayPoints.addLast(new WayPoint(x, y, WayPoint.Direction.WEST));
                }
                else if (dir == 5)
                {
                    wayPoints.addLast(new WayPoint(x, y, WayPoint.Direction.END));
                }
            }
        }
    }

    //constructs the paths between the waypoints, so that towers cannot be placed on the paths.
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

    //checks to see if the tower x,y is within a any path.
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
