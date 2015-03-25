package com.NewEntities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by James on 3/18/2015.
 */
public abstract class Entity extends Actor {
    protected Vector2 position;
    public int entityID;
    public int tempID;

    public static enum Type
    {
        ENEMY_NORMAL,
        ENEMY_FAST,
        ENEMY_HEAVY,
        TOWER_RIFLE,
        TOWER_BAZOOKA,
        TOWER_SNIPER
    }
    public Type type;

    public Entity(float x, float y)
    {
        position = new Vector2(x,y);
    }

    @Override
    public abstract void draw(Batch batch, float parentAlpha);

}
