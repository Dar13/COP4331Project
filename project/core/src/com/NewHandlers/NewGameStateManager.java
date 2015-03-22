package com.NewHandlers;

import com.NewStates.NewEnd;
import com.NewStates.NewGameState;
import com.NewStates.NewMenu;
import com.NewStates.NewNetTest;
import com.NewStates.NewPlay;
import com.NewStates.NewWinState;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.NetworkManager;

import java.util.Stack;

/**
 * Created by James on 3/18/2015.
 */
public class NewGameStateManager {
    private MyGame game;
    private Stack<NewGameState> gameStates;
    private NetworkManager networkManager;
    private boolean inAndroid = false;

    //A random number to represent PlayState.

    public static final int MENU = 131587867;
    public static final int PLAY = 321123321;
    public static final int BADEND = 1234321;
    public static final int GOODEND = 123123123;
    public static final int NET = 12341542;

    public NewGameStateManager(MyGame game, NetworkManager networkManager, boolean inAndroid)
    {
        this.inAndroid = inAndroid;
        this.game = game;
        this.networkManager = networkManager;

        gameStates = new Stack<NewGameState>();
        pushState(MENU);
    }

    public MyGame getGame()
    {
        return game;
    }

    public void update()
    {
        gameStates.peek().update();
    }

    public void render(float deltaTime)
    {
        gameStates.peek().render(deltaTime);
    }

    private NewGameState getState(int state)
    {
        if (state == PLAY)
        {
            return new NewPlay(this, networkManager, inAndroid);
        }
        if (state == MENU)
        {
            return new NewMenu(this,networkManager);
        }
        if(state == BADEND){
            return new NewEnd(this, networkManager);
        }
        if(state == GOODEND){
            return new NewWinState(this, networkManager);
        }
        if(state == NET) {
            return new NewNetTest(this, networkManager);
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
        NewGameState g = gameStates.pop();
        g.dispose();
    }

}
