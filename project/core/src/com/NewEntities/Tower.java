package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.entities.*;
import com.mygdx.entities.Enemy;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public abstract class Tower extends Entity
{
    // gameplay attributes
    protected float damage;
    protected float range;
    protected int price;
    protected float firingDelay;
    protected float timeSinceLastShot;

    // display attributes
    protected Sprite base;
    protected Sprite other;
    protected int target;
    protected float targetDistanceTraveled;

    public Tower(Type type, Texture baseTexture, Texture otherTexture, float x, float y)
    {
        super(x, y);

        this.type = type;
        this.timeSinceLastShot = 0;
        targetDistanceTraveled = 0;

        base = new Sprite(baseTexture);
        base.setPosition(x, y);

        other = new Sprite(otherTexture);
        other.setPosition(x + 9, y - 23);
        other.rotate(-45);
    }

    public abstract void draw(Batch batch, float parentAlpha);

    public float getTimeSinceLastShot()
    {
        return timeSinceLastShot;
    }

    public boolean readyToFire(){
        if(timeSinceLastShot > firingDelay){
            // Is this ok?
            //resetTimeSinceLastShot();
            return true;
        }

        return false;
    }

    public void updateTimeSinceLastShot()
    {
        timeSinceLastShot++;
    }

    public void resetTimeSinceLastShot()
    {
        timeSinceLastShot = 0;
    }

    public int getTarget()
    {
        return target;
    }

    public void setTarget(int targetID)
    {
        this.target = targetID;
    }

    public void setTargetDistanceTraveled(float targetDistanceTraveled)
    {
        this.targetDistanceTraveled = targetDistanceTraveled;
    }

    public float getTargetDistanceTraveled()
    {
        return targetDistanceTraveled;
    }

    public float getDamage(Type type)
    {
        switch (this.type) {
            case TOWER_BAZOOKA:
                switch (type){
                    case ENEMY_NORMAL:
                        return (damage / 3);
                    case ENEMY_FAST:
                        return (damage / 3);
                    case ENEMY_HEAVY:
                        return (damage * 2);
                }
                break;

            case TOWER_RIFLE:
                switch (type){
                    case ENEMY_NORMAL:
                        return damage;
                    case ENEMY_FAST:
                        return damage;
                    case ENEMY_HEAVY:
                        return (damage / 3);
                }
                break;

            case TOWER_SNIPER:
                switch (type){
                    case ENEMY_NORMAL:
                        return (damage * 2);
                    case ENEMY_FAST:
                        return damage;
                    case ENEMY_HEAVY:
                        return (damage / 2);
                }
                break;
            }
        return damage / 2;
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

    //Checks to see if the x, y coordinates input clash with the stored x, y coordinates.
    public boolean steppingOnToes(float x, float y)
    {
        Rectangle bounds = new Rectangle(base.getX(), base.getY(), 32, 32);
        Rectangle rect = new Rectangle(x, y, 32, 32);

        return bounds.overlaps(rect);
    }

    public void upgrade(float damageMultiplier, float rangeMultiplier)
    {
        damage *= damageMultiplier;
        range *= rangeMultiplier;
    }
}
