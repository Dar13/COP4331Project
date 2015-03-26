package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public class SniperTower extends Tower
{
    public static final float BASE_DAMAGE = 5.0f;
    public static final float BASE_RANGE = 5.0f;
    public static final int PRICE = 300;
    public static final float FIRING_DELAY = 10f;

    public SniperTower(Texture baseTexture, Texture otherTexture, float x, float y)
    {
        super(Type.TOWER_SNIPER, baseTexture, otherTexture, x, y);

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
