package com.mygdx.handlers;

import com.mygdx.triggers.WayPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * Created by LordNeah on 3/6/2015.
 */
public class WayPointManager {

    public LinkedList<WayPoint> wayPoints;
    public File inputtxt;

    public WayPointManager(){
        inputtxt = new File("Waypoints.txt");
        this.wayPoints = new LinkedList<WayPoint>();
        try{
            ReadInWaypoints();
        }
        catch (FileNotFoundException e){

        }
    }

    private void ReadInWaypoints() throws FileNotFoundException{
        Scanner in = new Scanner(inputtxt);
        in.useDelimiter(" ");
        int xx = 0;
        int yy = 0;
        String dir = new String();
        while (in.hasNext()) {
            if (in.hasNextInt()) {
                int x = in.nextInt();
                xx = x;
            }

            if (in.hasNextInt()) {
                int y = in.nextInt();
                yy = y;
            }

            if (in.hasNext()) {
                char direction = in.next().charAt(0);
                if (direction == 'n') {
                    wayPoints.addLast(new WayPoint(xx, yy, WayPoint.Direction.NORTH));
                } else if (direction == 's') {
                    wayPoints.addLast(new WayPoint(xx, yy, WayPoint.Direction.SOUTH));
                } else if (direction == 'e') {
                    wayPoints.addLast(new WayPoint(xx, yy, WayPoint.Direction.EAST));
                } else if (direction == 'w') {
                    wayPoints.addLast(new WayPoint(xx, yy, WayPoint.Direction.WEST));
                } else if (direction == 'o') {
                    wayPoints.addLast(new WayPoint(xx, yy, WayPoint.Direction.END));
                }
            }

        }

        in.close();
    }




}
