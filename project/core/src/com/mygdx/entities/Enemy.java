package com.mygdx.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.triggers.WayPoint;

import java.util.List;

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
    protected int currenttick;
    protected boolean navigationFinished;
    protected float distanceTraveled;


    TextureRegion[][] South;
    TextureRegion[][] North;
    TextureRegion[][] East;
    TextureRegion[][] West;
    protected Sprite current;

    private int ticker;
    private int ticker1;
    private int ticker2;

    public Enemy(Type type, float x, float y)
    {
        super(x, y);

        ticker = 0;
        ticker1 = 0;
        ticker2 = 0;
        health = BASE_HEALTH;

        this.type = type;
        distanceTraveled = 0;
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

        current.setX(position.x);
        current.setY(position.y);

        heading = wayPoints.get(currentWayPoint).direction;
    }

    public void move()
    {
        ticker2++;
        position.x += (velocity * heading.x);
        position.y += (velocity * heading.y);

        if(ticker2 == 10) {
            switch (wayPoints.get(currentWayPoint - 1).direction) {
                case NORTH:
                    current = new Sprite(North[ticker1][ticker]);
                    if(ticker == 3){
                        ticker1++;
                        ticker = 0;
                    }
                    else{
                        ticker++;
                    }
                    break;
                case SOUTH:
                    current = new Sprite(South[ticker1][ticker]);
                    if(ticker == 3){
                        ticker1++;
                        ticker = 0;
                    }
                    else{
                        ticker++;
                    }
                    break;
                case EAST:
                    current = new Sprite(East[ticker1][ticker]);
                    if(ticker == 3){
                        ticker1++;
                        ticker = 0;
                    }
                    else{
                        ticker++;
                    }
                    break;
                case WEST:
                    current = new Sprite(West[ticker1][ticker]);
                    if(ticker == 3){
                        ticker1++;
                        ticker = 0;
                    }
                    else{
                        ticker++;
                    }
                    break;
            }

            ticker2 = 0;
        }

        if(ticker1 == 3 && ticker == 3){
            ticker = 0;
            ticker1 = 0;
        }

        current.setPosition(position.x, position.y);

        distanceTraveled += Math.abs(velocity);
    }

    public void rotateToDirection(WayPoint.Direction direction)
    {
        Sprite temp = current;
        switch (direction){
            case NORTH:
                current = new Sprite(North[ticker1][ticker]);
                current.setPosition(temp.getX(), temp.getY());
                break;
            case SOUTH:
                current = new Sprite(South[ticker1][ticker]);
                current.setPosition(temp.getX(), temp.getY());
                break;
            case EAST:
                current = new Sprite(East[ticker1][ticker]);
                current.setPosition(temp.getX(), temp.getY());
                break;
            case WEST:
                current = new Sprite(West[ticker1][ticker]);
                current.setPosition(temp.getX(), temp.getY());
                break;
        }
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
                if (wayPoint.direction == WayPoint.Direction.RAND){
                    currentWayPoint = wayPoint.returnRandPath();
                    wayPoint = wayPoints.get(currentWayPoint);
                    System.out.println(currentWayPoint);
                }

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

    public void decrementVelocity(float decrement) {velocity = decrement;}

    public float getArmor()
    {
        return armor;
    }

    public float getHealth()
    {
        return health;
    }

    public void takeDamage(float damageAmount)
    {
        decrementHealth(damageAmount / armor);
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

    public float getDistanceTraveled()
    {
        return distanceTraveled;
    }

}
