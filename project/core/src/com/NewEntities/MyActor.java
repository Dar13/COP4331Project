package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by James on 3/19/2015.
 */
public class MyActor extends NewEntities {

    public MyActor(Texture img,float x,float y){
        super(img,x,y);
    }

    public Sprite getSprite(){return sprite;}

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite.getTexture(),sprite.getX(),sprite.getY());
    }
}
