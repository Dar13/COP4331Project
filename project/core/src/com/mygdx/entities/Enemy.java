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
    public Sprite sprite2;
    public float x = 0;
    public float y = 0;
    public float velocity = 0;
    public WayPoint.Direction heading;
    private float tolerance;
    public LinkedList<WayPoint> wayPoints;
    private int currentWaypoint = 0;
    public float health = 100;
    public float armor = 1;
    public boolean atEnd = false;

    //Enums for enemy type.
    public enum Type
    {
        HEAVY,
        NORMAL,
        FAST
    }

    public Type type;


    public Enemy(Texture img, Texture img2, float velocity, float armor, LinkedList<WayPoint> path, Type type)
    {
        super(img, path.getFirst().x, path.getFirst().y);
        currentWaypoint++;
        this.wayPoints = path;
        this.sprite = new Sprite(img);
        this.sprite2 = new Sprite(img2);
        this.velocity = velocity;
        this.tolerance = velocity / 2;
        this.type = type;
        switch (type){
            case HEAVY:
                this.tolerance = velocity + 16;
        }
        sprite.setPosition(x, y);
        sprite2.setPosition(x + 12, y + 2);
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
                sprite2.setPosition(sprite2.getX(), sprite2.getY() + velocity);
                break;
            case EAST:
                sprite.setPosition(sprite.getX() + velocity, sprite.getY());
                sprite2.setPosition(sprite2.getX() + velocity, sprite2.getY());
                break;
            case SOUTH:
                sprite.setPosition(sprite.getX(), sprite.getY() - velocity);
                sprite2.setPosition(sprite2.getX(), sprite2.getY() - velocity);
                break;
            case WEST:
                sprite.setPosition(sprite.getX() - velocity, sprite.getY());
                sprite2.setPosition(sprite2.getX() - velocity, sprite2.getY());
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
                        atEnd = true;
                        return true;
                    }
                    return false;
            }
        }
        return false;
    }

    //Rotating the sprites at each waypoint based on enemy type. Very broken at the moment.
    public void changeSpriteRotation()
    {
        switch (type)
        {
            case HEAVY:
            if (heading == WayPoint.Direction.EAST)
            {
                switch (wayPoints.get(currentWaypoint).direction)
                {
                    case NORTH:
                        sprite.rotate(90);
                        sprite.setPosition(wayPoints.get(currentWaypoint).x - 16, wayPoints.get(currentWaypoint).y);
                        sprite2.rotate(90);
                        sprite2.setPosition(wayPoints.get(currentWaypoint).x - 8, wayPoints.get(currentWaypoint).y + 6);
                        break;
                    case SOUTH:
                        sprite.rotate(-90);
                        sprite.setPosition(wayPoints.get(currentWaypoint).x + 16, wayPoints.get(currentWaypoint).y);
                        sprite2.rotate(-90);
                        sprite2.setPosition(wayPoints.get(currentWaypoint).x - 8, wayPoints.get(currentWaypoint).y + 6);
                        break;
                    case WEST:
                        sprite.rotate(180);
                        sprite2.rotate(180);
                        break;
                }
            }

            if (heading == WayPoint.Direction.WEST)
            {
                switch (wayPoints.get(currentWaypoint).direction)
                {
                    case NORTH:
                        sprite.rotate(-90);
                        sprite.setPosition(wayPoints.get(currentWaypoint).x + 16, wayPoints.get(currentWaypoint).y);
                        sprite2.rotate(-90);
                        sprite2.setPosition(wayPoints.get(currentWaypoint).x - 8, wayPoints.get(currentWaypoint).y + 6);
                        break;
                    case SOUTH:
                        sprite.rotate(90);
                        sprite.setPosition(wayPoints.get(currentWaypoint).x - 16, wayPoints.get(currentWaypoint).y);
                        sprite2.rotate(90);
                        sprite2.setPosition(wayPoints.get(currentWaypoint).x - 8, wayPoints.get(currentWaypoint).y - 2);
                        break;
                    case EAST:
                        sprite.rotate(180);
                        sprite2.rotate(180);
                        break;
                }
            }

            if (heading == WayPoint.Direction.NORTH)
            {
                switch (wayPoints.get(currentWaypoint).direction)
                {
                    case EAST:
                        sprite.rotate(-90);
                        sprite.setPosition(wayPoints.get(currentWaypoint).x + 16, wayPoints.get(currentWaypoint).y);
                        sprite2.rotate(-90);
                        sprite2.setPosition(wayPoints.get(currentWaypoint).x - 8, wayPoints.get(currentWaypoint).y + 2);
                        break;
                    case SOUTH:
                        sprite.rotate(180);
                        sprite2.rotate(180);
                        break;
                    case WEST:
                        sprite.rotate(90);
                        sprite.setPosition(wayPoints.get(currentWaypoint).x - 16, wayPoints.get(currentWaypoint).y);
                        sprite2.rotate(90);
                        sprite2.setPosition(wayPoints.get(currentWaypoint).x - 13, wayPoints.get(currentWaypoint).y + 3);
                        break;
                }
            }

            if (heading == WayPoint.Direction.SOUTH)
            {
                switch (wayPoints.get(currentWaypoint).direction)
                {
                    case NORTH:
                        sprite.rotate(180);
                        sprite2.rotate(180);
                        break;
                    case EAST:
                        sprite.rotate(90);
                        sprite.setPosition(wayPoints.get(currentWaypoint).x + 16, wayPoints.get(currentWaypoint).y);
                        sprite2.rotate(90);
                        sprite2.setPosition(wayPoints.get(currentWaypoint).x - 8, wayPoints.get(currentWaypoint).y + 6);
                        break;
                    case WEST:
                        sprite.rotate(-90);
                        sprite.setPosition(wayPoints.get(currentWaypoint).x + 16, wayPoints.get(currentWaypoint).y);
                        sprite2.rotate(-90);
                        sprite2.setPosition(wayPoints.get(currentWaypoint).x - 8, wayPoints.get(currentWaypoint).y + 6);
                        break;
                }
            }
            case NORMAL:
                if (heading == WayPoint.Direction.EAST)
                {
                    switch (wayPoints.get(currentWaypoint).direction)
                    {
                        case NORTH:
                            sprite.rotate(90);
                            sprite2.rotate(0);
                            break;
                        case SOUTH:
                            sprite.rotate(-90);
                            sprite2.rotate(0);
                            break;
                        case WEST:
                            sprite.rotate(180);
                            sprite2.rotate(180);
                            break;
                    }
                }

                if (heading == WayPoint.Direction.WEST)
                {
                    switch (wayPoints.get(currentWaypoint).direction)
                    {
                        case NORTH:
                            sprite.rotate(90);
                            sprite2.rotate(0);
                            break;
                        case SOUTH:
                            sprite.rotate(90);
                            sprite2.rotate(0);
                            break;
                        case EAST:
                            sprite.rotate(180);
                            sprite2.rotate(0);
                            break;
                    }
                }

                if (heading == WayPoint.Direction.NORTH)
                {
                    switch (wayPoints.get(currentWaypoint).direction)
                    {
                        case EAST:
                            sprite.rotate(-90);
                            sprite2.rotate(0);
                            break;
                        case SOUTH:
                            sprite.rotate(180);
                            sprite2.rotate(0);
                            break;
                        case WEST:
                            sprite.rotate(90);
                            sprite2.rotate(0);
                            break;
                    }
                }

                if (heading == WayPoint.Direction.SOUTH)
                {
                    switch (wayPoints.get(currentWaypoint).direction)
                    {
                        case NORTH:
                            sprite.rotate(90);
                            sprite2.rotate(90);
                            break;
                        case EAST:
                            sprite.rotate(-0);
                            sprite2.rotate(-0);
                            break;
                        case WEST:
                            sprite.rotate(0);
                            sprite2.rotate(0);
                            break;
                    }
                }
            case FAST:
                if (heading == WayPoint.Direction.EAST)
                {
                    switch (wayPoints.get(currentWaypoint).direction)
                    {
                        case NORTH:
                            sprite.rotate(-0);
                            sprite2.rotate(-0);
                            break;
                        case SOUTH:
                            sprite.rotate(0);
                            sprite2.rotate(0);
                            break;
                        case WEST:
                            sprite.rotate(90);
                            sprite2.rotate(90);
                            break;
                    }
                }

                if (heading == WayPoint.Direction.WEST)
                {
                    switch (wayPoints.get(currentWaypoint).direction)
                    {
                        case NORTH:
                            sprite.rotate(-0);
                            sprite2.rotate(-0);
                            break;
                        case SOUTH:
                            sprite.rotate(0);
                            sprite2.rotate(0);
                            break;
                        case EAST:
                            sprite.rotate(90);
                            sprite2.rotate(90);
                            break;
                    }
                }

                if (heading == WayPoint.Direction.NORTH)
                {
                    switch (wayPoints.get(currentWaypoint).direction)
                    {
                        case EAST:
                            sprite.rotate(-0);
                            sprite2.rotate(-0);
                            break;
                        case SOUTH:
                            sprite.rotate(90);
                            sprite2.rotate(90);
                            break;
                        case WEST:
                            sprite.rotate(0);
                            sprite2.rotate(0);
                            break;
                    }
                }

                if (heading == WayPoint.Direction.SOUTH)
                {
                    switch (wayPoints.get(currentWaypoint).direction)
                    {
                        case NORTH:
                            sprite.rotate(90);
                            sprite2.rotate(90);
                            break;
                        case EAST:
                            sprite.rotate(0);
                            sprite2.rotate(0);
                            break;
                        case WEST:
                            sprite.rotate(-0);
                            sprite2.rotate(-0);
                            break;
                    }
                }
        }
    }

    //Update the linked list contained in enemy.
    public void setWayPointsLL(LinkedList<WayPoint> wayPoints)
    {
        this.wayPoints = wayPoints;
    }

    public void render(SpriteBatch sb)
    {
        sprite.draw(sb);
        sprite2.draw(sb);
    }

}
