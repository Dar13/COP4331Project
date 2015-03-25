package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by James on 3/19/2015.
 */
public class MyActor extends Entity
{
    Sprite img;

    public MyActor(Texture texture,float x,float y)
    {
        super(x,y);
        this.img = new Sprite(texture);
        img.setPosition(x,y);
    }


    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.draw(img, img.getX(),img.getY());
    }
}
