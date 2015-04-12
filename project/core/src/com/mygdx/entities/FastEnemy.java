package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.triggers.WayPoint;

import java.util.List;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public class FastEnemy extends Enemy
{
    public static final float BASE_VELOCITY = 6.0f;
    public static final float BASE_ARMOR = 1.0f;
    TextureRegion[][] South;
    TextureRegion[][] North;
    TextureRegion[][] East;
    TextureRegion[][] West;
    private int ticker;
    private int ticker1;
    private int ticker2;

    public FastEnemy(Texture north, Texture south, Texture east, Texture west, float x, float y)
    {
        super(Type.ENEMY_FAST, x, y);

        ticker = 0;
        ticker2 = 0;

        South = TextureRegion.split(south, 32, 32);
        North = TextureRegion.split(north, 32, 32);
        East = TextureRegion.split(east, 32, 32);
        West = TextureRegion.split(west, 32, 32);

        this.current = new Sprite(east);

        velocity = BASE_VELOCITY;
        armor = BASE_ARMOR;

        navigationTolerance = velocity / 2.0f;
    }

    @Override
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

    @Override
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

    @Override
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

    public void draw(Batch batch, float parentAlpha)
    {
        current.draw(batch);
    }
}
