package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by Miguel on 2/20/2015.
 */
public class Tower extends Entities {
    public Sprite sprite;
    public float x=0;
    public float y=0;
    private float tolerance;
    public LinkedList<WayPoint> wayPoints;

    public Tower(Texture img, float x, float y, String heading){
        super(img,x, y);
        this.sprite = new Sprite(img);
        this.x = x;
        this.y = y;
        sprite.setPosition(x,y);
        wayPoints = new LinkedList<WayPoint>();
    }

    public void render(SpriteBatch sb){
        sprite.draw(sb);
    }

}
