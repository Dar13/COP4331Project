package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 2/14/2015.
 */
public class Enemy {
    public Sprite sprite;
    float x=0;
    float y=0;
    float velocity = 0;
    LinkedList<WayPoint> wayPoints;


    public Enemy(Texture img){
        this.sprite = new Sprite(img);
        sprite.setPosition(x,y);
    }
    public Enemy(Texture img, float x, float y, float velocity){
        this.sprite = new Sprite(img);
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        sprite.setPosition(x,y);
        wayPoints = new LinkedList<WayPoint>();
    }

    public void Move(){sprite.setPosition(sprite.getX()+velocity,sprite.getY());}
    public boolean Check(){
        /*if(sprite.getX() == wayPoints.getFirst().x && sprite.getY() == wayPoints.getFirst().y){
           velocity = -velocity;
        }*/
        if((sprite.getX() > (wayPoints.getFirst().x - 0.5f)) &&
           (sprite.getX() < (wayPoints.getFirst().x + 0.5f)) &&
           (sprite.getY() > (wayPoints.getFirst().y - 0.5f)) &&
           (sprite.getY() < (wayPoints.getFirst().y + 0.5f)))
        {

            sprite.setPosition(sprite.getX(),sprite.getY());
            sprite.setPosition(sprite.getX(),sprite.getY()+0.6f);
            velocity = - velocity;
            return true;
        }
        return false;
    }
    public void SetWayPoint(float x, float y){wayPoints.add(new WayPoint(x,y));}


    public void render(SpriteBatch sb){
        sprite.draw(sb);
    }

}
