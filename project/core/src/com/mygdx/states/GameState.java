package com.mygdx.states;

import com.mygdx.handlers.GameStateManager;
import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.net.NetworkManager;

/**
 * Created by James on 3/18/2015.
 */
public abstract class GameState implements Screen {
    protected GameStateManager gameStateManager;
    protected NetworkManager networkManager;
    protected MyGame game;

    protected GameState(GameStateManager gameStateManager,
                        NetworkManager networkManager){
        this.gameStateManager = gameStateManager;
        this.networkManager = networkManager;

        game = gameStateManager.getGame();
    }
    public abstract void update(float deltatime);


    @Override
    public void resize(int width, int height)
    {

    }
}
