package com.mygdx.triggers;

/**
 * Created by James on 2/20/2015.
 */
public class Path
{
    public float x1;
    public float y1;
    public float x2;
    public float y2;

    public Path(float x1, float y1, float x2, float y2)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    //Check to see if any corner of the tower is within the path.
    public boolean IsWithin(float x, float y)
    {

        if (x >= x1 && x <= x2 && y >= y1 && y <= y2)
        {
            return true;
        }

        else if (x + 32 >= x1 && x + 32 <= x2 && y >= y1 && y <= y2)
        {
            return true;
        }

        else if (x >= x1 && x <= x2 && y + 32 >= y1 && y + 32 <= y2)
        {
            return true;
        }

        else if (x + 32 >= x1 && x + 32 <= x2 && y + 32 >= y1 && y + 32 <= y2)
        {
            return true;
        }

        else
        {
            return false;
        }
    }
}
