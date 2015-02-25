package com.mygdx.handlers;

/**
 * Created by James on 2/22/2015.
 */
public class MyInput {
    public static int x;
    public static int y;
    public static boolean down;
    public static boolean pdown; //pervious down

    public static void update(){
        pdown = down;
    }

    public static boolean isDown(){ return down;}
    public static boolean isPressed(){ return down && !pdown;}
    public static boolean isReleased(){ return !down && pdown;}
}
