package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public class HeavyEnemy extends Enemy
{
    public static final float BASE_VELOCITY = .5f;
    public static final float BASE_ARMOR = 3.0f;

    public HeavyEnemy(Texture north, Texture south, Texture east, Texture west, float x, float y)
    {
        super(Type.ENEMY_HEAVY, x, y);

        this.north = new Sprite(north);
        this.south = new Sprite(south);
        this.east = new Sprite(east);
        this.west = new Sprite(west);
        this.current = new Sprite(east);

        velocity = BASE_VELOCITY;
        armor = BASE_ARMOR;

        navigationTolerance = velocity + 16f;
    }

    public void draw(Batch batch, float parentAlpha)
    {
        current.draw(batch);
    }
}
