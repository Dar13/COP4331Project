package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public abstract class Tower extends Entity
{
    // gameplay attributes
    protected float damage;
    protected float range;
    protected int price;

    // display attributes
    protected Sprite base;
    protected Sprite other;

    public Tower(Type type, Texture baseTexture, Texture otherTexture, float x, float y)
    {
        super(x, y);

        this.type = type;

        base = new Sprite(baseTexture);
        base.setPosition(x, y);

        other = new Sprite(otherTexture);
        other.setPosition(x + 9, y - 23);
        other.rotate(-45);
    }

    public abstract void draw(Batch batch, float parentAlpha);

    public float getDamage()
    {
        return damage;
    }

    public float getRange()
    {
        return range;
    }

    public int getPrice()
    {
        return price;
    }

    public boolean inRange(float x, float y, int positionOffset, int rangeOffset)
    {
        return inRange(new Vector2(x,y),positionOffset, rangeOffset);
    }

    public boolean inRange(Vector2 position, int positionOffset, int rangeOffset)
    {
        Vector2 ePosition = new Vector2(position);
        ePosition.x += positionOffset;
        ePosition.y += positionOffset;

        Vector2 tPosition = new Vector2(this.position);
        tPosition.x += positionOffset;
        tPosition.y += positionOffset;

        float nRange = range * rangeOffset;

        return (tPosition.sub(ePosition).len2() <= (nRange * nRange));
    }

    public boolean steppingOntoes(float x, float y)
    {
        if ((x > base.getX() && x < base.getX() + 32
                && y > base.getY() && y < base.getY() + 32)
                || (x + 32 > base.getX() && x + 32 < base.getX() + 32
                && y + 32 > base.getY() && y + 32 < base.getY() + 32)
                || (x > base.getX() && x < base.getX() + 32
                && y + 32 > base.getY() && y + 32 < base.getY() + 32)
                || (x + 32 > base.getX() && x + 32 < base.getX() + 32
                && y > base.getY() && y < base.getY() + 32))
        {
            return true;
        }
        return false;
    }

    public void upgrade(float damageMultiplier, float rangeMultiplier)
    {
        damage *= damageMultiplier;
        range *= rangeMultiplier;
    }
}
