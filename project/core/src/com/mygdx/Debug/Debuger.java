package com.mygdx.Debug;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.MyGame;

/**
 * Created by James on 2/20/2015.
 */
public class Debuger {
    public OrthographicCamera debugeCam;
    public ShapeRenderer shapeRenderer;

    public Debuger(){
        debugeCam = new OrthographicCamera();
        debugeCam.setToOrtho(false, MyGame.V_WIDTH,MyGame.V_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(debugeCam.combined);
    }

    public void render(){

    }
}
