package com.mygdx.handlers;

import com.mygdx.game.MyGame;
import com.mygdx.states.GameOver;
import com.mygdx.states.GameState;
import com.mygdx.states.Menu;
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
    private boolean inAndroid = false;

    //A random number to represent PlayState.
    public static final int PLAY = 388031654;
    public static final int MENU = 131587867;
    public static final int LOSE = 321123321;

    public GameStateManager(MyGame game, NetworkManager networkManager, boolean inAndroid)
    {
        this.inAndroid = inAndroid;
        this.game = game;
        this.networkManager = networkManager;

        gameStates = new Stack<GameState>();
        pushState(MENU);
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
        if (state == PLAY)
        {
            return new Play(this, networkManager, inAndroid);
        }
        if (state == MENU)
        {
            return new Menu(this, networkManager);
        }
        if (state == LOSE){
            return new GameOver(this, networkManager);
        }

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
