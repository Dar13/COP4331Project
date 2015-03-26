package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.triggers.WayPoint;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by NeilMoore on 3/24/2015.
 */
public abstract class Enemy extends Entity
{
    public static final int BASE_HEALTH = 100;

    // basic gameplay attirbutes
    protected float velocity;
    protected float armor;
    protected float health;

    // navigation attributes
    protected float navigationTolerance;
    protected WayPoint.Direction heading;
    protected List<WayPoint> wayPoints;
    protected int currentWayPoint;
    protected boolean navigationFinished;

    // These might need to be the other way around.
    // base = sprite, other = sprite2
    protected Sprite base;
    protected Sprite other;

    public Enemy(Type type, float x, float y)
    {
        super(x, y);

        health = BASE_HEALTH;
    }

    public abstract void draw(Batch batch, float parentAlpha);

    public void applyVelocityMultiplier(float mult)
    {
        velocity *= mult;

        // reset navigation tolerance
        navigationTolerance = velocity / 2.0f;
    }

    public void applyArmorMultiplier(float mult)
    {
        armor *= mult;
    }

    public void setWayPoints(List<WayPoint> waypoints)
    {
        wayPoints = waypoints;
        currentWayPoint = 0;

        position.x = wayPoints.get(currentWayPoint).x;
        position.y = wayPoints.get(currentWayPoint).y;

        base.setX(position.x);
        base.setY(position.y);

        other.setX(position.x);
        other.setY(position.y);

        heading = wayPoints.get(currentWayPoint).direction;
    }

    public void move()
    {
        position.x += (velocity * heading.x);
        position.y += (velocity * heading.y);

        base.setPosition(position.x, position.y);
        other.setPosition(position.x, position.y);
    }

    public void rotateToDirection(WayPoint.Direction direction)
    {
        Vector2 directionVec = new Vector2(direction.x, direction.y);
        Vector2 headingVec = new Vector2(heading.x, heading.y);

        float angle = directionVec.angle(headingVec);

        base.rotate(angle);
        other.rotate(angle);
    }

    public boolean check()
    {
        if(currentWayPoint >= 0 && currentWayPoint < wayPoints.size())
        {
            WayPoint wayPoint = wayPoints.get(currentWayPoint);
            Vector2 wpPosition = new Vector2(wayPoint.x, wayPoint.y);
            Vector2 curPosition = new Vector2(position);
            Vector2 distance = curPosition.sub(wpPosition);
            // within the tolerance radius of the waypoint
            if(distance.len2() <= (navigationTolerance * navigationTolerance))
            {
                position.x = wayPoint.x;
                position.y = wayPoint.y;

                rotateToDirection(wayPoint.direction);

                heading = wayPoint.direction;
                currentWayPoint++;

                if(wayPoint.direction == WayPoint.Direction.END)
                {
                    velocity = 0;
                    navigationFinished = true;
                }

                return true;
            }
            else
            {
                return false;
            }
        }

        return false;
    }

    public float getVelocity()
    {
        return velocity;
    }

    public float getArmor()
    {
        return armor;
    }

    public float getHealth()
    {
        return health;
    }

    public void decrementHealth(float amount)
    {
        health -= amount;
    }

    public void incrementHealth(float amount)
    {
        health += amount;
    }

    public boolean isAlive()
    {
        return (health > 0.0f);
    }

    public boolean isNavigationFinished()
    {
        return navigationFinished;
    }
}
