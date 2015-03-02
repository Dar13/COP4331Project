package com.mygdx.states;

// LibGDX includes

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by NeilMoore on 2/14/2015.
 */
public class NetTest extends GameState
{
    protected Texture image;
    protected boolean connected = false;

    public NetTest(GameStateManager gameStateManager, NetworkManager networkManager)
    {
        super(gameStateManager, networkManager);

        image = new Texture("badlogic.jpg");
    }

    @Override
    public void handleInput()
    {
    }

    @Override
    public void update(float dt)
    {
    }

    @Override
    public void render()
    {
        // Very basic render state for now.
        Gdx.gl.glClearColor(1, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(connected)
        {
            spriteBatch.begin();
            spriteBatch.draw(image, 0, 0);
            spriteBatch.end();
        }
    }

    @Override
    public void dispose()
    {

    }
}
