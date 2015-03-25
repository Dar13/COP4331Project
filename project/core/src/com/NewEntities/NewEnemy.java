package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 3/18/2015.
 */
public class NewEnemy extends Entity
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

    public NewEnemy(Texture img, Texture img2, float velocity, float armor, LinkedList<WayPoint> path, Type type){
        super(path.get(0).x, path.get(0).y);
        currentWaypoint++;
        this.wayPoints = path;
        this.sprite = new Sprite(img);
        this.sprite2 = new Sprite(img2);
        this.velocity = velocity;
        this.tolerance = velocity / 2;
        this.type = type;
        switch (type){
            case ENEMY_HEAVY:
                this.tolerance = velocity + 16;
                break;
            case ENEMY_NORMAL:
                break;
            case ENEMY_FAST:
                break;
        }
        sprite.setPosition(path.get(0).x, path.get(0).y);
        sprite2.setPosition(path.get(0).x + 12, path.get(0).y + 2);
        heading = wayPoints.getFirst().direction;
        this.armor = armor;
        switch(heading){
            case SOUTH:
                sprite.rotate(-90);
                sprite2.rotate(-90);
                break;
        }
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
            case ENEMY_HEAVY:
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

                    break;
                }
                else if (heading == WayPoint.Direction.WEST)
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

                    break;
                }
                else if (heading == WayPoint.Direction.NORTH)
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

                    break;
                }
                else if (heading == WayPoint.Direction.SOUTH)
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

                    break;
                }
            case ENEMY_NORMAL:
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
                            sprite.rotate(90);
                            sprite2.rotate(90);
                            break;
                    }

                    break;
                }

                else if (heading == WayPoint.Direction.WEST)
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

                    break;
                }

                else if (heading == WayPoint.Direction.NORTH)
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

                    break;
                }

                else if (heading == WayPoint.Direction.SOUTH)
                {
                    switch (wayPoints.get(currentWaypoint).direction)
                    {
                        case NORTH:
                            sprite.rotate(180);
                            sprite2.rotate(180);
                            break;
                        case EAST:
                            sprite.rotate(90);
                            sprite2.rotate(0);
                            break;
                        case WEST:
                            sprite.rotate(-90);
                            sprite2.rotate(0);
                            break;
                    }

                    break;
                }

            case ENEMY_FAST:
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
                            sprite.rotate(90);
                            sprite2.rotate(90);
                            break;
                    }

                    break;
                }

                else if (heading == WayPoint.Direction.WEST)
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

                    break;
                }

                else if (heading == WayPoint.Direction.NORTH)
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

                    break;
                }

                else if (heading == WayPoint.Direction.SOUTH)
                {
                    switch (wayPoints.get(currentWaypoint).direction)
                    {
                        case NORTH:
                            sprite.rotate(180);
                            sprite2.rotate(180);
                            break;
                        case EAST:
                            sprite.rotate(90);
                            sprite2.rotate(0);
                            break;
                        case WEST:
                            sprite.rotate(-90);
                            sprite2.rotate(0);
                            break;
                    }

                    break;
                }


        }
    }

    //Update the linked list contained in enemy.
    public void setWayPointsLL(LinkedList<WayPoint> wayPoints)
    {
        this.wayPoints = wayPoints;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
        sprite2.draw(batch);
    }
}
