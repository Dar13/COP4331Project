package com.mygdx.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.MyInput;

import java.awt.Button;

/**
 * Created by James on 3/24/2015.
 */
public class MyStage extends Stage {


    public MyStage(){
        super(new StretchViewport(MyGame.V_WIDTH,MyGame.V_HEIGHT));

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 temp = this.screenToStageCoordinates(new Vector2(screenX,screenY));
        MyInput.x = (int)temp.x;
        MyInput.y = (int)temp.y;
        MyInput.down = true;
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 temp = this.screenToStageCoordinates(new Vector2(screenX,screenY));
        MyInput.x = (int)temp.x;
        MyInput.y = (int)temp.y;
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 temp = this.screenToStageCoordinates(new Vector2(screenX,screenY));
        MyInput.x = (int)temp.x;
        MyInput.y = (int)temp.y;
        MyInput.down = false;
        return super.touchUp(screenX, screenY, pointer, button);
    }

   /* @Override
    public void draw() {
        Batch batch = this.getBatch();
        batch.begin();
        Array<Actor> actors = this.getActors();
        for (Actor a: actors){
            if (a instanceof TextButton){
                batch.setProjectionMatrix(hubCam.combined);
                a.draw(batch,1);
            }
            else{
                batch.setProjectionMatrix(this.getCamera().combined);
                a.draw(batch,1);
            }
        }
        this.getCamera().update();
       batch.end();
    }*/
}
