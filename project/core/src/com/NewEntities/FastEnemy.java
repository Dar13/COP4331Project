package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public class FastEnemy extends Enemy
{
    public static final float BASE_VELOCITY = 6.0f;
    public static final float BASE_ARMOR = 1.0f;

    public FastEnemy(Texture baseTexture, Texture otherTexture, float x, float y)
    {
        super(Type.ENEMY_FAST, x, y);

        base = new Sprite(baseTexture);
        other = new Sprite(otherTexture);

        velocity = BASE_VELOCITY;
        armor = BASE_ARMOR;

        navigationTolerance = velocity / 2.0f;
    }

    public void draw(Batch batch, float parentAlpha)
    {
        base.draw(batch);
        other.draw(batch);
    }
}