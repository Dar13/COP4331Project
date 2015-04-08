package com.mygdx.handlers;

import com.mygdx.states.End;
import com.mygdx.states.GameState;
import com.mygdx.states.LevelSelect;
import com.mygdx.states.Menu;
import com.mygdx.states.NetTest;
import com.mygdx.states.Play;
import com.mygdx.states.Tutorial;
import com.mygdx.states.WinState;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.NetworkManager;

import java.util.Stack;

/**
 * Created by James on 3/18/2015.
 */
public class GameStateManager {
    private MyGame game;
    private Stack<GameState> gameStates;
    private NetworkManager networkManager;
    private boolean inAndroid = false;

    //A random number to represent PlayState.

    public static final int MENU = 131587867;
    public static final int PLAY = 321123321;
    public static final int BADEND = 1234321;
    public static final int GOODEND = 123123123;
    public static final int NET = 12341542;
    public static final int LEVELSELECT = 321321321;
    public static final int TUTORIAL = 1337;

    public GameStateManager(MyGame game, NetworkManager networkManager, boolean inAndroid)
    {
        this.inAndroid = inAndroid;
        this.game = game;
        this.networkManager = networkManager;

        gameStates = new Stack<GameState>();
        pushState(MENU, 0);
    }

    public MyGame getGame()
    {
        return game;
    }

    public void update(float deltatime)
    {
        gameStates.peek().update(deltatime);
    }

    public void render(float deltaTime)
    {
        gameStates.peek().render(deltaTime);
    }

    private GameState getState(int state, int mapnum)
    {
        if (state == PLAY)
        {
            return new Play(this, networkManager, mapnum);
        }
        if (state == MENU)
        {
            return new Menu(this,networkManager);
        }
        if(state == BADEND){
            return new End(this, networkManager);
        }
        if(state == GOODEND){
            return new WinState(this, networkManager);
        }
        if(state == NET) {
            return new NetTest(this, networkManager);
        }
        if(state == LEVELSELECT){
            return new LevelSelect(this, networkManager);
        }
        if(state == TUTORIAL){
            return new Tutorial(this, networkManager);
        }
        return null;
    }

    public void setState(int state, int mapnum)
    {
        popState();
        pushState(state, mapnum);
    }

    public void pushState(int state, int mapnum)
    {
        gameStates.push(getState(state, mapnum));
    }

    public void popState()
    {
        GameState g = gameStates.pop();
        g.dispose();
    }

}
