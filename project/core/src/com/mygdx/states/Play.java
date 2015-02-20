package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.Debug.Debuger;
import com.mygdx.entities.Enemy;
import com.mygdx.entities.Tower;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 2/1/2015.
 */
public class Play extends GameState
{

    private  boolean debugMode_ON = true;
    private Texture img;
    private Enemy enemy;

    private Tower tower;
    public ShapeRenderer shapeRenderer;
    private OrthographicCamera cam;
    private Debuger debuger;
    private LinkedList<WayPoint> wayPoints;

    public Play(GameStateManager gsm)
    {
        super(gsm);

        wayPoints = new LinkedList<WayPoint>();
        wayPoints.addLast(new WayPoint(0,0,"e"));
        wayPoints.addLast(new WayPoint(MyGame.V_WIDTH-32,0,"n"));
        wayPoints.addLast(new WayPoint(MyGame.V_WIDTH-32,MyGame.V_HEIGHT-32,"w"));
        wayPoints.addLast(new WayPoint(0,MyGame.V_HEIGHT-32,"s"));
        wayPoints.addLast(new WayPoint(0,0,"end"));

        cam = new OrthographicCamera();
        cam.setToOrtho(false,MyGame.V_WIDTH,MyGame.V_HEIGHT);
        debuger = new Debuger(wayPoints);
        shapeRenderer = new ShapeRenderer();
        img = new Texture("EnemyDev.png");
        //EnemManager = new EnemyManager(5, 5, 5, 0, 0, "e");

        enemy= new Enemy(img,3,wayPoints);
        enemy.setWayPointsLL(wayPoints);
        img = new Texture("DevText_Tower.png");
        tower = new Tower(img, 50, 50);

    }

    public void handleInput()
    {
    }

    public void update(float deltaTime)
    {
        if(!enemy.Check()){
            enemy.Move();
        }
    }

    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.begin();
        enemy.render(spriteBatch);
        tower.render(spriteBatch);
        spriteBatch.end();
        shapeRenderer.end();

        if(debugMode_ON){
            debuger.render();
        }

    }

    public void dispose(){};

}
