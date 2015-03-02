package com.mygdx.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by James on 2/1/2015.
 */
public abstract class GameState
{
    protected GameStateManager gameStateManager;
    protected NetworkManager networkManager;
    protected MyGame game;

    protected SpriteBatch spriteBatch;
    protected OrthographicCamera orthoCamera;
    protected OrthographicCamera hudCamera;

    protected GameState(GameStateManager gameStateManager,
                        NetworkManager networkManager)
    {
        this.gameStateManager = gameStateManager;
        this.networkManager = networkManager;

        game = gameStateManager.getGame();
        spriteBatch = game.getSpriteBatch();
        orthoCamera = game.getCamera();
        hudCamera = game.getHudCamera();
    }

    public abstract void handleInput();

    public abstract void update(float deltaTime);

    public abstract void render();

    public abstract void dispose();
}
