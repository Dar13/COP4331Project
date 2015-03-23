package com.NewStates;

import com.NewHandlers.NewGameStateManager;
import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by James on 3/18/2015.
 */
public abstract class NewGameState implements Screen {
    protected NewGameStateManager gameStateManager;
    protected NetworkManager networkManager;
    protected MyGame game;

    protected NewGameState(NewGameStateManager gameStateManager,
                           NetworkManager networkManager){
        this.gameStateManager = gameStateManager;
        this.networkManager = networkManager;

        game = gameStateManager.getGame();
    }
    public abstract void update();
}
