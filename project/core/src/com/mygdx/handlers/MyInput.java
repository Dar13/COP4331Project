package com.mygdx.handlers;

/**
 * Created by James on 2/22/2015.
 */
public class MyInput
{
    public static int x;
    public static int y;
    public static boolean down;
    public static boolean prevDown;

    public static void update()
    {
        prevDown = down;
    }

    public static boolean isDown()
    {
        return down;
    }

    public static boolean isPressed()
    {
        return down && !prevDown;
    }

    public static boolean isReleased()
    {
        return !down && prevDown;
    }
}
