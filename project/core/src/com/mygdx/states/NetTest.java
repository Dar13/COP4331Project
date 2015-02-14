package com.mygdx.states;

// LibGDX includes
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.mygdx.handlers.GameStateManager;

/**
 * Created by NeilMoore on 2/14/2015.
 */
public class NetTest extends GameState
{
  public NetTest(GameStateManager gameStateManager)
  {
    super(gameStateManager);

    // need to bring in the network module somehow.
    // Refactor GameState's constructor?
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
  }

  @Override
  public void dispose()
  {

  }
}
