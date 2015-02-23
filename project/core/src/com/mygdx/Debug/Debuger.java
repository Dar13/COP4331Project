package com.mygdx.Debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.entities.Tower;
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
    private LinkedList<Tower> towers;
    private boolean END = false;
    int waypointindex = 0;

    public Debuger(LinkedList<WayPoint> path, LinkedList<Tower> towers){
        debugeCam = new OrthographicCamera();
        debugeCam.setToOrtho(false, MyGame.V_WIDTH,MyGame.V_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(debugeCam.combined);
        this.path = path;
        this.towers = towers;
    }

    public void render(){
        debugeCam.update();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        while(END != true) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(path.get(waypointindex).x, path.get(waypointindex).y, path.get(waypointindex + 1).x - path.get(waypointindex).x + 32, path.get(waypointindex +1).y - path.get(waypointindex).y + 32);
            switch (path.get(waypointindex+1).direction){
                case ("end"):
                    END = true;
                    break;
            }
            waypointindex++;
        }

        int i = 0;
        while(i < towers.size()) {
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.circle(towers.get(i).x + 16, towers.get(i).y + 16, towers.get(i).range * 32);
            i++;
        }

        shapeRenderer.end();
        waypointindex = 0;
        END = false;
    }
}
