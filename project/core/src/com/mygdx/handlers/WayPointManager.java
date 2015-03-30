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
    private int[] map1 = new int[]{ 0, 448, 2, 0, 0, 3, 96, 0,
                                    1, 96, 448, 3, 192, 448, 2,
                                    192, 0, 3, 288, 0, 1, 288,
                                    448, 3, 384, 448, 2, 384, 0,
                                    3, 480, 0, 1, 480, 448, 3, 576,
                                    448, 2, 576, 256, 3, 608, 256, 5};
    private int[] map2 = new int[]{ 0, 352, 3, 480, 352, 2, 480,
                                    96, 4, 160, 96, 1, 160, 352,
                                    3, 480, 352, 2, 480, 96, 4,
                                    160, 96, 1, 160, 352, 3, 480,
                                    352, 2, 480, 96, 3, 608, 96, 5};

    private int mapnum = 0;



    public WayPointManager(int MapLoad)
    {
        mapnum = MapLoad;
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
        int[] maptemp = null;

            switch (mapnum){
                case 1:
                    maptemp = map1;
                    break;
                case 2:
                    maptemp = map2;
                    break;
                case 3:
                    //int[] maptemp = map3;
                    break;
                case 4:
                    //int[] maptemp = map4;
                    break;
            }


            for(int i = 0; i < map1.length; i ++)
            {
                int x = maptemp[i];
                i++;
                int y = maptemp[i];
                i++;
                int dir = maptemp[i];
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
                    paths.addLast(NEW);
                }
                else
                {
                    Path NEW = new Path(wayPoints.get(i).x, wayPoints.get(i).y, wayPoints.get(i+1).x + 32, wayPoints.get(i+1).y + 32);
                    paths.addLast(NEW);
                }
            }

            else if (wayPoints.get(i).y == wayPoints.get(i+1).y)
            {
                if(wayPoints.get(i).x > wayPoints.get(i+1).x)
                {
                    Path NEW = new Path(wayPoints.get(i+1).x, wayPoints.get(i+1).y, wayPoints.get(i).x + 32, wayPoints.get(i).y + 32);
                    paths.addLast(NEW);
                }
                else
                {
                    Path NEW = new Path(wayPoints.get(i).x, wayPoints.get(i).y, wayPoints.get(i+1).x + 32, wayPoints.get(i+1).y + 32);
                    paths.addLast(NEW);
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
            i++;
        }


        return false;
    }

}
