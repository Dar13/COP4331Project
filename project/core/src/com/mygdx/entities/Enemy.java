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
    String heading;
   public LinkedList<WayPoint> wayPoints;


    public Enemy(Texture img){
        this.sprite = new Sprite(img);
        sprite.setPosition(x,y);
    }
    public Enemy(Texture img, float x, float y, float velocity, String heading){
        this.sprite = new Sprite(img);
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.heading = heading;
        sprite.setPosition(x,y);
        wayPoints = new LinkedList<WayPoint>();
    }

    public void Move(){
        switch (heading){
            case ("n"):
                sprite.setPosition(sprite.getX(),sprite.getY()+velocity);
                break;
            case ("e"):
                sprite.setPosition(sprite.getX()+velocity,sprite.getY());
                break;
            case ("s"):
                sprite.setPosition(sprite.getX(),sprite.getY()-velocity);
                break;
            case ("w"):
                sprite.setPosition(sprite.getX()-velocity,sprite.getY());
                break;
        }
    }
    public void Check(){
       switch (wayPoints.getFirst().dirction){
           case ("n"):
               if(sprite.getX() == wayPoints.getFirst().x && sprite.getY() == wayPoints.getFirst().y){
                   heading = "n";
               }
               break;
           case ("e"):
               if(sprite.getX() == wayPoints.getFirst().x && sprite.getY() == wayPoints.getFirst().y){
                   heading = "e";
               }
               break;
           case ("s"):
               if(sprite.getX() == wayPoints.getFirst().x && sprite.getY() == wayPoints.getFirst().y){
                   heading = "s";
               }
               break;
           case ("w"):
               if(sprite.getX() == wayPoints.getFirst().x && sprite.getY() == wayPoints.getFirst().y){
                   heading = "w";
               }
               break;
       }

    }
    public void SetWayPoint(float x, float y, String direction){wayPoints.add(new WayPoint(x,y,direction));}


    public void render(SpriteBatch sb){
        sprite.draw(sb);
    }

}
