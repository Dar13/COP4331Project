package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.entities.Enemy;
import com.mygdx.handlers.GameStateManager;

/**
 * Created by James on 2/1/2015.
 */
public class Play extends GameState
{
    private Texture img;
    private Enemy enemy;

    public Play(GameStateManager gsm)
    {
        super(gsm);
        img = new Texture("EnemyDev.png");
        enemy= new Enemy(img,0,0,3);
        enemy.SetWayPoint(240,0);
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
        spriteBatch.begin();
        enemy.render(spriteBatch);
        spriteBatch.end();
    }

    public void dispose()
    {
    }
}
