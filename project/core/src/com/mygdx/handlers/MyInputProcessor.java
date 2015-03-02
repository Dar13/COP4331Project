package com.mygdx.handlers;

import com.badlogic.gdx.InputAdapter;

/**
 * Created by James on 2/22/2015.
 */
public class MyInputProcessor extends InputAdapter
{

    public boolean mouseMoved(int x, int y)
    {
        return true;
    }

    public boolean touchDragged(int x, int y, int pointer)
    {
        MyInput.x = x;
        MyInput.y = y;
        System.out.println(x + " " + y);
        return true;
    }

    public boolean touchDown(int x, int y, int pointer, int button)
    {
        MyInput.x = x;
        MyInput.y = y;
        MyInput.down = true;
        return true;
    }

    public boolean touchUp(int x, int y, int pointer, int button)
    {
        MyInput.x = x;
        MyInput.y = y;
        MyInput.down = false;
        return true;
    }

    public boolean keyDown(int k)
    {
        return true;
    }

    public boolean keyUp(int k)
    {
        return true;
    }
}
