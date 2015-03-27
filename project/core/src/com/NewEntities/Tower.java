package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    protected int targetDistanceTraveled;

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

    public float getTimeSinceLastShot(){
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

    public int returnTarget() { return target; }

    public void setTargetDistanceTraveled(int targetDistanceTraveled){ this.targetDistanceTraveled = targetDistanceTraveled; }

    public int getTargetDistanceTraveled(){ return targetDistanceTraveled; }

    public void setTarget(int targetID) {
        this.target = targetID;
    }

    public int returnTarget() { return target; }

    public void setTargetDistanceTraveled(int targetDistanceTraveled){ this.targetDistanceTraveled = targetDistanceTraveled; }

    public int getTargetDistanceTraveled(){ return targetDistanceTraveled; }

    public void setTarget(int targetID) {
        this.target = targetID;
    }

    public float getDamage(Type type)
    {
        switch (this.type) {
            case TOWER_BAZOOKA:
                switch (type){
                    case ENEMY_NORMAL:
                        return damage / 3;
                    case ENEMY_FAST:
                        return damage / 3;
                    case ENEMY_HEAVY:
                        return damage * 2;
                }

            case TOWER_RIFLE:
                switch (type){
                    case ENEMY_NORMAL:
                        return damage;
                    case ENEMY_FAST:
                        return damage;
                    case ENEMY_HEAVY:
                        return damage / 3;
                }


            case TOWER_SNIPER:
                switch (type){
                    case ENEMY_NORMAL:
                        return damage * 2;
                    case ENEMY_FAST:
                        return damage;
                    case ENEMY_HEAVY:
                        return damage / 2;
                }
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

    public float returnX(){ return base.getX(); }

    public float returnY(){ return base.getY(); }

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
