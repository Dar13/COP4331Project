package com.mygdx.states;

import com.mygdx.handlers.GameStateManager;
import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.QueueCallback;
import com.mygdx.handlers.action.Action;
import com.mygdx.handlers.net.NetworkManager;
import com.mygdx.handlers.net.QueueManager;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by James on 3/18/2015.
 */
public abstract class GameState implements Screen, QueueCallback {
    protected GameStateManager gameStateManager;
    protected NetworkManager networkManager;
    protected MyGame game;

    protected AtomicInteger queueCallbackStatus;
    protected ArrayList<Action> changes;

    protected GameState(GameStateManager gameStateManager,
                        NetworkManager networkManager){
        this.gameStateManager = gameStateManager;
        this.networkManager = networkManager;

        game = gameStateManager.getGame();

        changes = null;

        queueCallbackStatus = new AtomicInteger(QueueCallbackStatus.CALLBACK_STATUS_NO_REQUEST.ordinal());
    }
    public abstract void update(float deltatime);


    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void addCompleted()
    {

    }

    @Override
    public void retrieved(Object o, QueueManager.QueueID id)
    {
        changes = (ArrayList<Action>) o;
        queueCallbackStatus.set(QueueCallbackStatus.CALLBACK_STATUS_RESULTS_WAITING.ordinal());
    }
}
