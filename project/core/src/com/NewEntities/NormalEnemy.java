package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.triggers.WayPoint;

/**
 * Created by NeilMoore on 3/24/2015.
 */
public class NormalEnemy extends Enemy
{
    public static final float BASE_VELOCITY = 3.0f;
    public static final float BASE_ARMOR = 1.0f;

    public NormalEnemy(Texture baseImage, Texture otherImage, float x, float y)
    {
        super(Type.ENEMY_NORMAL, x, y);

        base = new Sprite(baseImage);
        other = new Sprite(otherImage);

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
