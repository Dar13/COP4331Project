package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 2/14/2015.
 */
public class Enemy extends  Entities {
    public Sprite sprite;
    public float x=0;
    public float y=0;
    public float velocity = 0;
    public String heading;
    private float tolerance;
    public LinkedList<WayPoint> wayPoints;


    public Enemy(Texture img, float x, float y, float velocity, String heading){
        super(img,x, y);
        this.sprite = new Sprite(img);
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.tolerance = velocity/2;
        this.heading = heading;
        sprite.setPosition(x,y);
        wayPoints = new LinkedList<WayPoint>();
    }

    /*
        Move the enemy based on the enemy's heading and its velocity.
     */
    public void Move() {
        switch (heading) {
            case ("n"):
                sprite.setPosition(sprite.getX(), sprite.getY() + velocity);
                break;
            case ("e"):
                sprite.setPosition(sprite.getX() + velocity, sprite.getY());
                break;
            case ("s"):
                sprite.setPosition(sprite.getX(), sprite.getY() - velocity);
                break;
            case ("w"):
                sprite.setPosition(sprite.getX() - velocity, sprite.getY());
                break;
        }
    }
    /*
        The Check function looks at the top of wayPoints and sees if the enemy's position is within
    wayPoints' tolerance and then moves the player in the appropriate heading out side the
    wayPoints' tolerance.
    */
    public boolean Check(){
      if(!wayPoints.isEmpty()){
          switch (wayPoints.getFirst().direction){
              case ("n"):
                  if((sprite.getX() > (wayPoints.getFirst().x - tolerance)) &&
                          (sprite.getX() < (wayPoints.getFirst().x + tolerance)) &&
                          (sprite.getY() > (wayPoints.getFirst().y - tolerance)) &&
                          (sprite.getY() < (wayPoints.getFirst().y + tolerance)))
                  {

                      sprite.setPosition(wayPoints.getFirst().x,wayPoints.getFirst().y);
                      heading = "n";
                      wayPoints.removeFirst();
                      return true;
                  }
                  return false;
              case ("e"):
                  if((sprite.getX() > (wayPoints.getFirst().x - tolerance)) &&
                          (sprite.getX() < (wayPoints.getFirst().x + tolerance)) &&
                          (sprite.getY() > (wayPoints.getFirst().y - tolerance)) &&
                          (sprite.getY() < (wayPoints.getFirst().y + tolerance)))
                  {

                      sprite.setPosition(wayPoints.getFirst().x,wayPoints.getFirst().y);
                      heading = "e";
                      wayPoints.removeFirst();
                      return true;
                  }
                  return false;
              case ("s"):
                  if((sprite.getX() > (wayPoints.getFirst().x - tolerance)) &&
                          (sprite.getX() < (wayPoints.getFirst().x + tolerance)) &&
                          (sprite.getY() > (wayPoints.getFirst().y - tolerance)) &&
                          (sprite.getY() < (wayPoints.getFirst().y + tolerance)))
                  {

                      sprite.setPosition(wayPoints.getFirst().x,wayPoints.getFirst().y);
                      heading = "s";
                      wayPoints.removeFirst();
                      return true;
                  }
                  return false;
              case ("w"):
                  if((sprite.getX() > (wayPoints.getFirst().x - tolerance)) &&
                          (sprite.getX() < (wayPoints.getFirst().x + tolerance)) &&
                          (sprite.getY() > (wayPoints.getFirst().y - tolerance)) &&
                          (sprite.getY() < (wayPoints.getFirst().y + tolerance)))
                  {

                      sprite.setPosition(wayPoints.getFirst().x,wayPoints.getFirst().y);
                      heading = "w";
                      wayPoints.removeFirst();
                      return true;
                  }
              case ("end"):
                  if((sprite.getX() > (wayPoints.getFirst().x - tolerance)) &&
                          (sprite.getX() < (wayPoints.getFirst().x + tolerance)) &&
                          (sprite.getY() > (wayPoints.getFirst().y - tolerance)) &&
                          (sprite.getY() < (wayPoints.getFirst().y + tolerance)))
                  {
                      wayPoints.removeFirst();
                      velocity = 0;
                      // Deconstruction of the enemy and sprite is done here.
                      return true;
                  }
                  return false;
          }
      }
      return  false;
    }
    public void SetWayPoint(float x, float y, String direction){wayPoints.addLast(new WayPoint(x,y,direction));}


    public void render(SpriteBatch sb){
        sprite.draw(sb);
    }

}
