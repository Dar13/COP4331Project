package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.entities.Enemy;
import com.mygdx.game.EnemyManager;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.GameStateManager;

/**
 * Created by James on 2/1/2015.
 */
public class Play extends GameState
{
    private Texture img;
    private Enemy enemy;
    private EnemyManager EnemManager;
    public ShapeRenderer shapeRenderer;
    private OrthographicCamera cam;

    public Play(GameStateManager gsm)
    {
        super(gsm);
        cam = new OrthographicCamera();
        cam.setToOrtho(false,MyGame.V_WIDTH,MyGame.V_WIDTH);
        shapeRenderer = new ShapeRenderer();
        img = new Texture("EnemyDev.png");
        EnemManager = new EnemyManager(5, 5, 5, 0, 0, "e");
        enemy= new Enemy(img,0,0,3,"e");
        enemy.SetWayPoint(240,0,"n");
        enemy.SetWayPoint(240,160,"w");
        enemy.SetWayPoint(160,160,"s");
        enemy.SetWayPoint(160,50,"w");
        enemy.SetWayPoint(15,50,"n");
        enemy.SetWayPoint(15,240,"end");

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
        shapeRenderer.setProjectionMatrix(cam.combined);
        spriteBatch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1,0,0,0);
        shapeRenderer.rect(0,0,240+32,32);
        enemy.render(spriteBatch);
        spriteBatch.end();
        shapeRenderer.end();
    }

    public void dispose()
    {
    }
}
