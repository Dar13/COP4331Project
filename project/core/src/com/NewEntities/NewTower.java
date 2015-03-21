package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 3/20/2015.
 */
public class NewTower extends NewEntities {
    public Sprite sprite;
    public Sprite sprite2;
    public float x = 0;
    public float y = 0;
    public float damages = 0;
    public float range = 0;
    public LinkedList<WayPoint> wayPoints;

    public enum Type
    {
        RIFLE,
        BAZOOKA,
        SNIPER
    }

    public Type type;

    public NewTower(Texture img, Texture img2, float x, float y, float damages, float range, Type type) {
        super(img, x, y);
        this.type = type;
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite2.getTexture(),sprite2.getX(),sprite2.getY());
        batch.draw(sprite.getTexture(),sprite.getX(),sprite.getY());
    }
}
