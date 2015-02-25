package com.mygdx.UI;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.handlers.MyInput;

/**
 * Created by James on 2/22/2015.
 */
public class GameButton {
    private float x;
    private float y;
    public float width;
    public float height;

    private Sprite sprite;

    private Texture img;
    private Texture img2;

    private Vector3 vec;
    private OrthographicCamera cam;

    private boolean clicked;

    public GameButton(float x, float y, OrthographicCamera cam)
    {
        img = new Texture("EnemyDev.png");
        img2 = new Texture("button_down.png");

        this.x = x;
        this.y = y;
        this.cam = cam;

        width = img.getWidth();
        height = img.getHeight();
        vec = new Vector3();
    }

    public  boolean isClicked(){return clicked;}
    public void update(float dt){
        vec.set(MyInput.x,MyInput.y,0);
        cam.unproject(vec);

        if(MyInput.isDown() &&
                vec.x > x - width / 2 && vec.x < x + width / 2 &&
                vec.y > y - height / 2 && vec.y < y + height / 2)
        {
            clicked = true;
        }
        else
        {
            clicked = false;
        }
    }

    public void render(SpriteBatch sb)
    {

        sb.begin();
        if(clicked){
            if(clicked){
                sb.draw(img2,x,y);
            }
            else {
                sb.draw(img,x, y);
            }
        }
        sb.end();
    }

}
