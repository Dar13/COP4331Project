package com.NewUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.MyInput;

/**
 * Created by James on 3/24/2015.
 */
public class MyStage extends Stage {

    public MyStage(){
        super(new StretchViewport(MyGame.V_WIDTH,MyGame.V_HEIGHT));

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        MyInput.x = screenX;
        MyInput.y = screenY;
        MyInput.down = true;
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        MyInput.x = screenX;
        MyInput.y = screenY;
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        MyInput.x = screenX;
        MyInput.y = screenY;
        MyInput.down = false;
        return super.touchUp(screenX, screenY, pointer, button);
    }
}
