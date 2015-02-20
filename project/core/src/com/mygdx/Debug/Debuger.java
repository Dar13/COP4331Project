package com.mygdx.Debug;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.MyGame;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 2/20/2015.
 */
public class Debuger {
    public OrthographicCamera debugeCam;
    public ShapeRenderer shapeRenderer;
    private LinkedList<WayPoint> path;

    public Debuger(LinkedList<WayPoint> path){
        debugeCam = new OrthographicCamera();
        debugeCam.setToOrtho(false, MyGame.V_WIDTH,MyGame.V_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(debugeCam.combined);
        this.path =path;
    }

    public void render(){
        debugeCam.update();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1,0,0,0);
        shapeRenderer.rect(0,0,320,32);
        shapeRenderer.end();
    }
}
