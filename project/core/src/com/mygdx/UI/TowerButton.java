package com.mygdx.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.handlers.MyInput;

/**
 * Created by LordNeah on 3/7/2015.
 */
public class TowerButton {
    private float x;
    private float y;
    public float width;
    public float height;

    public float x_offset;
    public float y_offset;

    private Texture img;
    private BitmapFont font;

    private Vector3 vec;
    private OrthographicCamera cam;

    public boolean clicked = false;
    private boolean isReleased;

    public TowerButton(Texture img, float x, float y, OrthographicCamera cam)
    {
        this.img = img;
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.scale(3);

        this.x = x;
        this.y = y;
        this.cam = cam;

        width = img.getWidth();
        height = img.getHeight();
        vec = new Vector3();
        x_offset = x - width / 2;
        y_offset = y - height / 2;
    }

    public boolean isReleased()
    {
        return isReleased;
    }

    public boolean isClicked(){
        return isClicked();
    }

    public void update(float dt)
    {
        vec.set(MyInput.x, MyInput.y, 0);
        cam.unproject(vec);

        // NOTE: what does this code do?
        if (MyInput.isDown() &&
                vec.x > x - width / 2 &&
                vec.x < x + width / 2 &&
                vec.y > y - height / 2 &&
                vec.y < y + height / 2)
        {
            clicked = true;
        }
        else
        {
            clicked = false;
        }

        // NOTE: what does this code do?
        if (MyInput.isReleased() &&
                vec.x > x - width / 2 &&
                vec.x < x + width / 2 &&
                vec.y > y - height / 2 &&
                vec.y < y + height / 2)
        {
            isReleased = true;
        }
        else
        {
            isReleased = false;
        }

    }

    public void render(SpriteBatch sb)
    {
        if (clicked)
        {
            sb.draw(img, x_offset, y_offset);
        }
        else
        {
            sb.draw(img, x_offset, y_offset);
        }
    }
}
