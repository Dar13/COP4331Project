package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by James on 3/19/2015.
 */
public class Actor extends Entity
{
    protected Sprite sprite;

    public Actor(Texture texture, float x, float y)
    {
        super(x,y);
        this.sprite = new Sprite(texture);
        sprite.setPosition(x, y);
    }


    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.draw(sprite, sprite.getX(), sprite.getY());
    }
}
