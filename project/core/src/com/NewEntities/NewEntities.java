package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by James on 3/18/2015.
 */
public abstract class NewEntities extends Actor {
    public Sprite sprite;
    protected float x;
    protected float y;
    public int entityID;

    public NewEntities(Texture img, float x, float y)
    {
        sprite = new Sprite(img);
        this.x = x;
        this.y = y;
    }

}
