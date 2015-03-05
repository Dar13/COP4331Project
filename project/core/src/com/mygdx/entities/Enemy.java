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
    public static final int ARMOR = 1;

    public Sprite sprite;
    public float x = 0;
    public float y = 0;
    public float velocity = 0;
    public WayPoint.Direction heading;
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
        case NORTH:
            sprite.setPosition(sprite.getX(), sprite.getY() + velocity);
            break;
        case EAST:
            sprite.setPosition(sprite.getX() + velocity, sprite.getY());
            break;
        case SOUTH:
            sprite.setPosition(sprite.getX(), sprite.getY() - velocity);
            break;
        case WEST:
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
            case NORTH:
                if ((sprite.getX() > (wayPoints.get(currentWaypoint).x - tolerance)) &&
                        (sprite.getX() < (wayPoints.get(currentWaypoint).x + tolerance)) &&
                        (sprite.getY() > (wayPoints.get(currentWaypoint).y - tolerance)) &&
                        (sprite.getY() < (wayPoints.get(currentWaypoint).y + tolerance)))
                {

                    sprite.setPosition(wayPoints.get(currentWaypoint).x, wayPoints.get(currentWaypoint).y);
                    changeSpriteRotation();
                    heading = WayPoint.Direction.NORTH;
                    currentWaypoint++;
                    return true;
                }
                return false;
            case EAST:
                if ((sprite.getX() > (wayPoints.get(currentWaypoint).x - tolerance)) &&
                        (sprite.getX() < (wayPoints.get(currentWaypoint).x + tolerance)) &&
                        (sprite.getY() > (wayPoints.get(currentWaypoint).y - tolerance)) &&
                        (sprite.getY() < (wayPoints.get(currentWaypoint).y + tolerance)))
                {

                    sprite.setPosition(wayPoints.get(currentWaypoint).x, wayPoints.get(currentWaypoint).y);
                    changeSpriteRotation();
                    heading = WayPoint.Direction.EAST;
                    currentWaypoint++;
                    return true;
                }
                return false;
            case SOUTH:
                if ((sprite.getX() > (wayPoints.get(currentWaypoint).x - tolerance)) &&
                        (sprite.getX() < (wayPoints.get(currentWaypoint).x + tolerance)) &&
                        (sprite.getY() > (wayPoints.get(currentWaypoint).y - tolerance)) &&
                        (sprite.getY() < (wayPoints.get(currentWaypoint).y + tolerance)))
                {

                    sprite.setPosition(wayPoints.get(currentWaypoint).x, wayPoints.get(currentWaypoint).y);
                    changeSpriteRotation();
                    heading = WayPoint.Direction.SOUTH;
                    currentWaypoint++;
                    return true;
                }
                return false;
            case WEST:
                if ((sprite.getX() > (wayPoints.get(currentWaypoint).x - tolerance)) &&
                        (sprite.getX() < (wayPoints.get(currentWaypoint).x + tolerance)) &&
                        (sprite.getY() > (wayPoints.get(currentWaypoint).y - tolerance)) &&
                        (sprite.getY() < (wayPoints.get(currentWaypoint).y + tolerance)))
                {

                    sprite.setPosition(wayPoints.get(currentWaypoint).x, wayPoints.get(currentWaypoint).y);
                    changeSpriteRotation();
                    heading = WayPoint.Direction.WEST;
                    currentWaypoint++;

                    return true;
                }
                return false;
            case END:
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
        if (heading == WayPoint.Direction.EAST &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.NORTH)
        {
            sprite.rotate(90);
        }
        if (heading == WayPoint.Direction.EAST &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.SOUTH)
        {
            sprite.rotate(-90);
        }
        if (heading == WayPoint.Direction.EAST &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.WEST)
        {
            sprite.rotate(180);
        }
        if (heading == WayPoint.Direction.WEST &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.NORTH)
        {
            sprite.rotate(-90);
        }
        if (heading == WayPoint.Direction.WEST &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.SOUTH)
        {
            sprite.rotate(90);
        }
        if (heading == WayPoint.Direction.WEST &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.EAST)
        {
            sprite.rotate(180);
        }
        if (heading == WayPoint.Direction.NORTH &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.EAST)
        {
            sprite.rotate(-90);
        }
        if (heading == WayPoint.Direction.NORTH &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.SOUTH)
        {
            sprite.rotate(180);
        }
        if (heading == WayPoint.Direction.NORTH &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.WEST)
        {
            sprite.rotate(90);
        }
        if (heading == WayPoint.Direction.SOUTH &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.NORTH)
        {
            sprite.rotate(180);
        }
        if (heading == WayPoint.Direction.SOUTH &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.EAST)
        {
            sprite.rotate(90);
        }
        if (heading == WayPoint.Direction.SOUTH &&
            wayPoints.get(currentWaypoint).direction == WayPoint.Direction.WEST)
        {
            sprite.rotate(-90);
        }

    }

    public void setWayPointsLL(LinkedList<WayPoint> wayPoints)
    {
        this.wayPoints = wayPoints;
    }

    public void SetWayPoint(float x, float y, WayPoint.Direction direction)
    {
        wayPoints.addLast(new WayPoint(x, y, direction));
    }

    public void render(SpriteBatch sb)
    {
        sprite.draw(sb);
    }

}
