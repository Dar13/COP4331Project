package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by James on 2/16/2015.
 */
public abstract class Entities {
    protected Sprite sprite;
    protected float x;
    protected float y;

    protected Entities(Texture img,float x,float y){
        sprite = new Sprite(img);
        this.x = x;
        this.y = y;
    }

    public abstract void render(SpriteBatch sb);




}
