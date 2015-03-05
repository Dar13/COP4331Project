package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by Miguel on 2/20/2015.
 */

//May need to implement try catch for waypoints
public class Tower extends Entities
{
    public Sprite sprite;
    public Sprite sprite2;
    public float x = 0;
    public float y = 0;
    public float damages = 0;
    public float range = 0;
    public LinkedList<WayPoint> wayPoints;

    public Tower(Texture img, Texture img2, float x, float y, float damages, float range)
    {
        super(img, x, y);
        this.sprite = new Sprite(img);
        this.sprite2 = new Sprite(img2);
        this.x = x;
        this.y = y;
        this.damages = damages;
        this.range = range;
        sprite.setPosition(x, y);
        sprite2.setPosition(x + 9, y - 23);
        sprite2.rotate(-45);
        wayPoints = new LinkedList<WayPoint>();
    }

    public void shoot()
    {
        //to calculate center of enemy sprite:
        //public int xCenter = ( enemy.getX()+(enemy.getWidth()/2) );
        //public int yCenter = ( enemy.getY()+(enemy.getHeight()/2) );
    }

    public void update(float deltaTime)
    {
        //blah
    }

    public void render(SpriteBatch sb)
    {
        sprite2.draw(sb);
        sprite.draw(sb);
    }

}
