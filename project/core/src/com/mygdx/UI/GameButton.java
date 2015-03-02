package com.mygdx.UI;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.handlers.MyInput;

/**
 * Created by James on 2/22/2015.
 *
 * NOTE: look into converting this to libGDX internal UI API. more options for later editing.
 */
public class GameButton
{
    private float x;
    private float y;
    public float width;
    public float height;

    public float x_offset;
    public float y_offset;

    private Texture img;
    private Texture img2;

    private Vector3 vec;
    private OrthographicCamera cam;

    private boolean clicked;
    private boolean isReleased;

    public GameButton(float x, float y, OrthographicCamera cam)
    {
        img = new Texture("button.png");
        img2 = new Texture("button_down.png");

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
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        if (clicked)
        {
            sb.draw(img2, x_offset, y_offset);
        }
        else
        {
            sb.draw(img, x_offset, y_offset);
        }
        sb.end();
    }

}
