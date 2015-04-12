package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by NeilMoore on 3/24/2015.
 */
public class NormalEnemy extends Enemy
{
    public static final float BASE_VELOCITY = 3.0f;
    public static final float BASE_ARMOR = .75f;

    public NormalEnemy(Texture north, Texture south, Texture east, Texture west, float x, float y)
    {
        super(Type.ENEMY_NORMAL, x, y);

        South = TextureRegion.split(south, 32, 32);
        North = TextureRegion.split(north, 32, 32);
        East = TextureRegion.split(east, 32, 32);
        West = TextureRegion.split(west, 32, 32);
        this.current = new Sprite(east);

        velocity = BASE_VELOCITY;
        armor = BASE_ARMOR;

        navigationTolerance = velocity / 2.0f;
    }

    public void draw(Batch batch, float parentAlpha)
    {
        current.draw(batch);
    }
}
