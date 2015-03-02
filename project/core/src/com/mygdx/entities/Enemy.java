package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 2/14/2015.
 */
public class Enemy extends Entities
{
    public static final int VELOCITY = 3;
    public static final int ARMOR = 8;

    public Sprite sprite;
    public float x = 0;
    public float y = 0;
    public float velocity = 0;
    public String heading; // NOTE: needs to change to use enum rather than string. more efficient that way
    private float tolerance;
    public LinkedList<WayPoint> wayPoints;
    private int currentWaypoint = 0;
    public float health = 100;
    public float armor = 1;


    public Enemy(Texture img, float velocity, float armor, LinkedList<WayPoint> path)
    {
        super(img, path.getFirst().x, path.getFirst().y);
        currentWaypoint++;
        this.wayPoints = path;
        this.sprite = new Sprite(img);
        this.velocity = velocity;
        this.tolerance = velocity / 2;
        sprite.setPosition(x, y);
        heading = wayPoints.getFirst().direction;
        this.armor = armor;
    }

    /*
        Move the enemy based on the enemy's heading and its velocity.
     */
    public void Move()
    {
        switch (heading)
        {
        case ("n"):
            sprite.setPosition(sprite.getX(), sprite.getY() + velocity);
            break;
        case ("e"):
            sprite.setPosition(sprite.getX() + velocity, sprite.getY());
            break;
        case ("s"):
            sprite.setPosition(sprite.getX(), sprite.getY() - velocity);
            break;
        case ("w"):
            sprite.setPosition(sprite.getX() - velocity, sprite.getY());
            break;
        }
    }

    /*
        The Check function looks at the top of wayPoints and sees if the enemy's position is within
    wayPoints' tolerance and then moves the player in the appropriate heading out side the
    wayPoints' tolerance.
    */
    public boolean Check()
    {
        if (!wayPoints.isEmpty())
        {
            switch (wayPoints.get(currentWaypoint).direction)
            {
            case ("n"):
                if ((sprite.getX() > (wayPoints.get(currentWaypoint).x - tolerance)) &&
                        (sprite.getX() < (wayPoints.get(currentWaypoint).x + tolerance)) &&
                        (sprite.getY() > (wayPoints.get(currentWaypoint).y - tolerance)) &&
                        (sprite.getY() < (wayPoints.get(currentWaypoint).y + tolerance)))
                {

                    sprite.setPosition(wayPoints.get(currentWaypoint).x, wayPoints.get(currentWaypoint).y);
                    changeSpriteRotation();
                    heading = "n";
                    currentWaypoint++;
                    return true;
                }
                return false;
            case ("e"):
                if ((sprite.getX() > (wayPoints.get(currentWaypoint).x - tolerance)) &&
                        (sprite.getX() < (wayPoints.get(currentWaypoint).x + tolerance)) &&
                        (sprite.getY() > (wayPoints.get(currentWaypoint).y - tolerance)) &&
                        (sprite.getY() < (wayPoints.get(currentWaypoint).y + tolerance)))
                {

                    sprite.setPosition(wayPoints.get(currentWaypoint).x, wayPoints.get(currentWaypoint).y);
                    changeSpriteRotation();
                    heading = "e";
                    currentWaypoint++;
                    return true;
                }
                return false;
            case ("s"):
                if ((sprite.getX() > (wayPoints.get(currentWaypoint).x - tolerance)) &&
                        (sprite.getX() < (wayPoints.get(currentWaypoint).x + tolerance)) &&
                        (sprite.getY() > (wayPoints.get(currentWaypoint).y - tolerance)) &&
                        (sprite.getY() < (wayPoints.get(currentWaypoint).y + tolerance)))
                {

                    sprite.setPosition(wayPoints.get(currentWaypoint).x, wayPoints.get(currentWaypoint).y);
                    changeSpriteRotation();
                    heading = "s";
                    currentWaypoint++;
                    return true;
                }
                return false;
            case ("w"):
                if ((sprite.getX() > (wayPoints.get(currentWaypoint).x - tolerance)) &&
                        (sprite.getX() < (wayPoints.get(currentWaypoint).x + tolerance)) &&
                        (sprite.getY() > (wayPoints.get(currentWaypoint).y - tolerance)) &&
                        (sprite.getY() < (wayPoints.get(currentWaypoint).y + tolerance)))
                {

                    sprite.setPosition(wayPoints.get(currentWaypoint).x, wayPoints.get(currentWaypoint).y);
                    changeSpriteRotation();
                    heading = "w";
                    currentWaypoint++;

                    return true;
                }
                return false;
            case ("end"):
                if ((sprite.getX() > (wayPoints.get(currentWaypoint).x - tolerance)) &&
                        (sprite.getX() < (wayPoints.get(currentWaypoint).x + tolerance)) &&
                        (sprite.getY() > (wayPoints.get(currentWaypoint).y - tolerance)) &&
                        (sprite.getY() < (wayPoints.get(currentWaypoint).y + tolerance)))
                {

                    velocity = 0;

                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void changeSpriteRotation()
    {
        // NOTE: this needs to be changed to use enums instead of strings. This is comparing references, not value!
        if (heading == "e" && wayPoints.get(currentWaypoint).direction == "n")
        {
            sprite.rotate(90);
        }
        if (heading == "e" && wayPoints.get(currentWaypoint).direction == "s")
        {
            sprite.rotate(-90);
        }
        if (heading == "e" && wayPoints.get(currentWaypoint).direction == "w")
        {
            sprite.rotate(180);
        }
        if (heading == "w" && wayPoints.get(currentWaypoint).direction == "n")
        {
            sprite.rotate(-90);
        }
        if (heading == "w" && wayPoints.get(currentWaypoint).direction == "s")
        {
            sprite.rotate(90);
        }
        if (heading == "w" && wayPoints.get(currentWaypoint).direction == "e")
        {
            sprite.rotate(180);
        }
        if (heading == "n" && wayPoints.get(currentWaypoint).direction == "e")
        {
            sprite.rotate(-90);
        }
        if (heading == "n" && wayPoints.get(currentWaypoint).direction == "s")
        {
            sprite.rotate(180);
        }
        if (heading == "n" && wayPoints.get(currentWaypoint).direction == "w")
        {
            sprite.rotate(90);
        }
        if (heading == "s" && wayPoints.get(currentWaypoint).direction == "n")
        {
            sprite.rotate(180);
        }
        if (heading == "s" && wayPoints.get(currentWaypoint).direction == "e")
        {
            sprite.rotate(90);
        }
        if (heading == "s" && wayPoints.get(currentWaypoint).direction == "w")
        {
            sprite.rotate(-90);
        }

    }

    public void setWayPointsLL(LinkedList<WayPoint> wayPoints)
    {
        this.wayPoints = wayPoints;
    }

    public void SetWayPoint(float x, float y, String direction)
    {
        wayPoints.addLast(new WayPoint(x, y, direction));
    }

    public void render(SpriteBatch sb)
    {
        sprite.draw(sb);
    }

}
