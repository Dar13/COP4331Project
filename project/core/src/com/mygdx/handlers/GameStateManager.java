package com.mygdx.handlers;

import com.mygdx.game.MyGame;
import com.mygdx.states.GameState;
import com.mygdx.states.Play;

import java.util.Stack;

/**
 * Created by James on 2/1/2015.
 */
public class GameStateManager
{
    private MyGame game;
    private Stack<GameState> gameStates;
    private NetworkManager networkManager;

    //A random number to represent PlayState.
    public static final int PLAY = 388031654;

    public GameStateManager(MyGame game, NetworkManager networkManager)
    {
        this.game = game;
        this.networkManager = networkManager;

        gameStates = new Stack<GameState>();
        pushState(PLAY);
    }

    public MyGame getGame()
    {
        return game;
    }

    public void update(float deltaTime)
    {
        gameStates.peek().update(deltaTime);
    }

    public void render()
    {
        gameStates.peek().render();
    }

    private GameState getState(int state)
    {
        if (state == PLAY) return new Play(this, networkManager);

        return null;
    }

    public void setState(int state)
    {
        popState();
        pushState(state);
    }

    public void pushState(int state)
    {
        gameStates.push(getState(state));
    }

    public void popState()
    {
        GameState g = gameStates.pop();
        g.dispose();
    }

}
