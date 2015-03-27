package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public class BazookaTower extends Tower
{
    public static final float BASE_DAMAGE = 45.0f;
    public static final float BASE_RANGE = 2.0f;
    public static final int PRICE = 150;
    public static final float FIRING_DELAY = 60f;

    public BazookaTower(Texture baseTexture, Texture otherTexture, float x, float y)
    {
        super(Type.TOWER_BAZOOKA, baseTexture, otherTexture, x, y);

        damage = BASE_DAMAGE;
        range = BASE_RANGE;
        price = PRICE;
        firingDelay = FIRING_DELAY;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        other.draw(batch);
        base.draw(batch);
    }


}
