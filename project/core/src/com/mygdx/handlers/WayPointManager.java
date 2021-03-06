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
    private int[] map3 = new int[]{0, 448, 2, 0, 352, 3, 96, 352, 2,
                                   96, 256, 3, 192, 256, 2, 192, 160, 3,
                                   352, 160, 1, 352, 256, 3, 448, 256, 1,
                                   448, 352, 3, 544, 352, 1, 544, 448, 3,
                                   608, 448, 5};
    private int[] map4 = new int[]{0, 225, 3, 128, 225, 6, 2, 11, 20, 29,
                                   128, 225, 1, 128, 416, 3, 320, 416, 2,
                                   320, 255, 3, 450, 255, 1, 450, 416, 3,
                                   544, 416, 2, 544, 232, 3, 608, 232, 5,
                                   128, 225, 1, 128, 288, 3, 255, 288, 1,
                                   255, 448, 3, 360, 448, 2, 360, 288, 3,
                                   544, 288, 2, 544, 232, 3, 608, 232, 5,
                                   128, 225, 2, 128, 191, 3, 250, 191, 1,
                                   250, 223, 4, 228, 223, 2, 228, 191, 3,
                                   544, 191, 1, 544, 232, 3, 608, 232, 5,
                                   128, 225, 2, 128, 0, 3, 544, 0, 1,
                                   544, 232, 3, 608, 232, 5};

    private int[] submenumap = new int[]{0, 0, 3, 672, 0, 1, 672, 480, 4,
                                         0, 480, 2, 0, 32, 5};

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
                    maptemp = map3;
                    break;
                case 4:
                    maptemp = map4;
                    break;
                case 5:
                    maptemp = submenumap;
                    break;
            }


            for(int i = 0; i < maptemp.length; i ++)
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
                else if (dir == 6)
                {
                    i++;
                    int path1 = maptemp[i];
                    i++;
                    int path2 = maptemp[i];
                    i++;
                    int path3 = maptemp[i];
                    i++;
                    int path4 = maptemp[i];
                    wayPoints.addLast(new WayPoint(x, y, WayPoint.Direction.RAND));
                    wayPoints.getLast().randWaypointInfo(path1, path2, path3, path4);
                }
            }
    }

    //constructs the paths between the waypoints, so that towers cannot be placed on the paths.
    private void ConstructPaths()
    {
        if (mapnum == 4)
        {
            Path NEW = new Path(0, 225, 160, 257);
            paths.addLast(NEW);
            NEW = new Path(128, 0, 160, 480);
            paths.addLast(NEW);
            NEW = new Path(544, 0, 576, 480);
            paths.addLast(NEW);
            NEW = new Path(544, 225, 680, 257);
            paths.addLast(NEW);
        }

        else
        {
        int i = 0;
        while (i < wayPoints.size() - 1)
        {
            if (wayPoints.get(i).x == wayPoints.get(i + 1).x)
            {
                if (wayPoints.get(i).y > wayPoints.get(i + 1).y)
                {
                    Path NEW = new Path(wayPoints.get(i + 1).x, wayPoints.get(i + 1).y, wayPoints.get(i).x + 32, wayPoints.get(i).y + 32);
                    paths.addLast(NEW);
                }
                else
                {
                    Path NEW = new Path(wayPoints.get(i).x, wayPoints.get(i).y, wayPoints.get(i + 1).x + 32, wayPoints.get(i + 1).y + 32);
                    paths.addLast(NEW);
                }
            }
            else if (wayPoints.get(i).y == wayPoints.get(i + 1).y)
            {
                if (wayPoints.get(i).x > wayPoints.get(i + 1).x)
                {
                    Path NEW = new Path(wayPoints.get(i + 1).x, wayPoints.get(i + 1).y, wayPoints.get(i).x + 32, wayPoints.get(i).y + 32);
                    paths.addLast(NEW);
                }
                else
                {
                    Path NEW = new Path(wayPoints.get(i).x, wayPoints.get(i).y, wayPoints.get(i + 1).x + 32, wayPoints.get(i + 1).y + 32);
                    paths.addLast(NEW);
                }
            }
            i++;
        }
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
