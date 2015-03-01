package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by LordNeah on 3/1/2015.
 */
public class EnemyHeavy extends Entities {

    public Sprite sprite;
    public Sprite sprite2;
    public float x=0;
    public float y=0;
    public float velocity = 0;
    public String heading;
    private float tolerance;
    public LinkedList<WayPoint> wayPoints;
    private int CurrWaypoint = 0;
    public float health = 100;
    public float armor = 1;


    public EnemyHeavy(Texture img, Texture img2, float velocity, float armor, LinkedList<WayPoint> path){
        super(img,path.getFirst().x, path.getFirst().y);
        CurrWaypoint++;
        this.wayPoints = path;
        this.sprite = new Sprite(img);
        this.sprite2 = new Sprite(img2);
        this.velocity = velocity;
        this.tolerance = velocity + 16;
        sprite.setPosition(x,y);
        sprite2.setPosition(x + 12, y + 2);
        heading = wayPoints.getFirst().direction;
        this.armor = armor;
    }


    /*
        Move the enemy based on the enemy's heading and its velocity.
     */
    public void Move() {
        switch (heading) {
            case ("n"):
                sprite.setPosition(sprite.getX(), sprite.getY() + velocity);
                sprite2.setPosition(sprite2.getX(), sprite2.getY() + velocity);
                break;
            case ("e"):
                sprite.setPosition(sprite.getX() + velocity, sprite.getY());
                sprite2.setPosition(sprite2.getX() + velocity, sprite2.getY());
                break;
            case ("s"):
                sprite.setPosition(sprite.getX(), sprite.getY() - velocity);
                sprite2.setPosition(sprite2.getX(), sprite2.getY() - velocity);
                break;
            case ("w"):
                sprite.setPosition(sprite.getX() - velocity, sprite.getY());
                sprite2.setPosition(sprite2.getX() - velocity, sprite2.getY());
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
            switch (wayPoints.get(CurrWaypoint).direction){
                case ("n"):
                    if((sprite.getX() > (wayPoints.get(CurrWaypoint).x - tolerance)) &&
                            (sprite.getX() < (wayPoints.get(CurrWaypoint).x + tolerance)) &&
                            (sprite.getY() > (wayPoints.get(CurrWaypoint).y - tolerance)) &&
                            (sprite.getY() < (wayPoints.get(CurrWaypoint).y + tolerance)))
                    {

                        sprite.setPosition(wayPoints.get(CurrWaypoint).x,wayPoints.get(CurrWaypoint).y);
                        changeSpriteRotation();
                        heading = "n";
                        CurrWaypoint++;
                        return true;
                    }
                    return false;
                case ("e"):
                    if((sprite.getX() > (wayPoints.get(CurrWaypoint).x - tolerance)) &&
                            (sprite.getX() < (wayPoints.get(CurrWaypoint).x + tolerance)) &&
                            (sprite.getY() > (wayPoints.get(CurrWaypoint).y - tolerance)) &&
                            (sprite.getY() < (wayPoints.get(CurrWaypoint).y + tolerance)))
                    {

                        sprite.setPosition(wayPoints.get(CurrWaypoint).x,wayPoints.get(CurrWaypoint).y);
                        changeSpriteRotation();
                        heading = "e";
                        CurrWaypoint++;
                        return true;
                    }
                    return false;
                case ("s"):
                    if((sprite.getX() > (wayPoints.get(CurrWaypoint).x - tolerance)) &&
                            (sprite.getX() < (wayPoints.get(CurrWaypoint).x + tolerance)) &&
                            (sprite.getY() > (wayPoints.get(CurrWaypoint).y - tolerance)) &&
                            (sprite.getY() < (wayPoints.get(CurrWaypoint).y + tolerance)))
                    {

                        sprite.setPosition(wayPoints.get(CurrWaypoint).x,wayPoints.get(CurrWaypoint).y);
                        changeSpriteRotation();
                        heading = "s";
                        CurrWaypoint++;
                        return true;
                    }
                    return false;
                case ("w"):
                    if((sprite.getX() > (wayPoints.get(CurrWaypoint).x - tolerance)) &&
                            (sprite.getX() < (wayPoints.get(CurrWaypoint).x + tolerance)) &&
                            (sprite.getY() > (wayPoints.get(CurrWaypoint).y - tolerance)) &&
                            (sprite.getY() < (wayPoints.get(CurrWaypoint).y + tolerance)))
                    {

                        sprite.setPosition(wayPoints.get(CurrWaypoint).x,wayPoints.get(CurrWaypoint).y);
                        changeSpriteRotation();
                        heading = "w";
                        CurrWaypoint++;

                        return true;
                    }
                    return false;
                case ("end"):
                    if((sprite.getX() > (wayPoints.get(CurrWaypoint).x - tolerance)) &&
                            (sprite.getX() < (wayPoints.get(CurrWaypoint).x + tolerance)) &&
                            (sprite.getY() > (wayPoints.get(CurrWaypoint).y - tolerance)) &&
                            (sprite.getY() < (wayPoints.get(CurrWaypoint).y + tolerance)))
                    {

                        velocity = 0;

                        return true;
                    }
                    return false;
            }
        }
        return  false;
    }

    public void changeSpriteRotation(){
        if(heading == "e" && wayPoints.get(CurrWaypoint).direction == "n"){
            sprite.rotate(90);
            sprite.setPosition(wayPoints.get(CurrWaypoint).x - 16, wayPoints.get(CurrWaypoint).y);
            sprite2.rotate(90);
            sprite2.setPosition(wayPoints.get(CurrWaypoint).x - 8, wayPoints.get(CurrWaypoint).y + 6);
        }
        if(heading == "e" && wayPoints.get(CurrWaypoint).direction == "s"){
            sprite.rotate(-90);
            sprite.setPosition(wayPoints.get(CurrWaypoint).x + 16, wayPoints.get(CurrWaypoint).y);
            sprite2.rotate(-90);
            sprite2.setPosition(wayPoints.get(CurrWaypoint).x - 8, wayPoints.get(CurrWaypoint).y + 6);
        }
        if(heading == "e" && wayPoints.get(CurrWaypoint).direction == "w"){
            sprite.rotate(180);
            sprite2.rotate(180);
        }
        if(heading == "w" && wayPoints.get(CurrWaypoint).direction == "n"){
            sprite.rotate(-90);
            sprite.setPosition(wayPoints.get(CurrWaypoint).x + 16, wayPoints.get(CurrWaypoint).y);
            sprite2.rotate(-90);
            sprite2.setPosition(wayPoints.get(CurrWaypoint).x - 8, wayPoints.get(CurrWaypoint).y + 6);
        }
        if(heading == "w" && wayPoints.get(CurrWaypoint).direction == "s"){
            sprite.rotate(90);
            sprite.setPosition(wayPoints.get(CurrWaypoint).x - 16, wayPoints.get(CurrWaypoint).y);
            sprite2.rotate(90);
            sprite2.setPosition(wayPoints.get(CurrWaypoint).x - 8, wayPoints.get(CurrWaypoint).y -2);
        }
        if(heading == "w" && wayPoints.get(CurrWaypoint).direction == "e"){
            sprite.rotate(180);
            sprite2.rotate(180);
        }
        if(heading == "n" && wayPoints.get(CurrWaypoint).direction == "e"){
            sprite.rotate(-90);
            sprite.setPosition(wayPoints.get(CurrWaypoint).x + 16, wayPoints.get(CurrWaypoint).y);
            sprite2.rotate(-90);
            sprite2.setPosition(wayPoints.get(CurrWaypoint).x - 8, wayPoints.get(CurrWaypoint).y + 2);

        }
        if(heading == "n" && wayPoints.get(CurrWaypoint).direction == "s"){
            sprite.rotate(180);
            sprite2.rotate(180);
        }
        if(heading == "n" && wayPoints.get(CurrWaypoint).direction == "w"){
            sprite.rotate(90);
            sprite.setPosition(wayPoints.get(CurrWaypoint).x - 16, wayPoints.get(CurrWaypoint).y);
            sprite2.rotate(90);
            sprite2.setPosition(wayPoints.get(CurrWaypoint).x - 13, wayPoints.get(CurrWaypoint).y + 3);

        }
        if(heading == "s" && wayPoints.get(CurrWaypoint).direction == "n"){
            sprite.rotate(180);
            sprite2.rotate(180);
        }
        if(heading == "s" && wayPoints.get(CurrWaypoint).direction == "e"){
            sprite.rotate(90);
            sprite.setPosition(wayPoints.get(CurrWaypoint).x + 16, wayPoints.get(CurrWaypoint).y);
            sprite2.rotate(90);
            sprite2.setPosition(wayPoints.get(CurrWaypoint).x - 8, wayPoints.get(CurrWaypoint).y + 6);

        }
        if(heading == "s" && wayPoints.get(CurrWaypoint).direction == "w"){
            sprite.rotate(-90);
            sprite.setPosition(wayPoints.get(CurrWaypoint).x + 16, wayPoints.get(CurrWaypoint).y);
            sprite2.rotate(-90);
            sprite2.setPosition(wayPoints.get(CurrWaypoint).x - 8, wayPoints.get(CurrWaypoint).y + 6);

        }

    }

    public void setWayPointsLL(LinkedList<WayPoint> wayPoints){this.wayPoints = wayPoints;}
    public void SetWayPoint(float x, float y, String direction){wayPoints.addLast(new WayPoint(x,y,direction));}

    public void render(SpriteBatch sb)
    {
        sprite.draw(sb);
        sprite2.draw(sb);
    }














}
