package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by James on 2/1/2015.
 */
public class Play extends GameState
{
    private Texture img;

    public Play(GameStateManager gameStateManager, NetworkManager networkManager)
    {
        super(gameStateManager, networkManager);
        img = new Texture("badlogic.jpg");
    }

    public void handleInput()
    {
    }

    public void update(float deltaTime)
    {
    }

    public void render()
    {
        Gdx.gl.glClearColor(1, 0, 0, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(img, 0, 0);
        spriteBatch.end();
    }

    public void dispose()
    {
    }
}
