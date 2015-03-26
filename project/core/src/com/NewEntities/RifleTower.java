package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public class RifleTower extends Tower
{
    public static final float BASE_DAMAGE = 1.0f;
    public static final float BASE_RANGE = 2.0f;
    public static final int PRICE = 100;

    public RifleTower(Texture baseTexture, Texture otherTexture, float x, float y)
    {
        super(Type.TOWER_RIFLE, baseTexture, otherTexture, x, y);

        damage = BASE_DAMAGE;
        range = BASE_RANGE;
        price = PRICE;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        base.draw(batch);
        other.draw(batch);
    }
}
