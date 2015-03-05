package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.MyInput;
import com.mygdx.handlers.MyInputProcessor;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.net.NetworkInterface;

import java.util.HashMap;

public class MyGame extends ApplicationAdapter
{
    public static final int V_WIDTH = 320 * 2;
    public static final int V_HEIGHT = 240 * 2;
    public static final int SCALE = 2;

    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private GameStateManager gameStateManager;

    private double accumulator;
    private float step = 1.0f / 60.0f;

    private NetworkManager networkManager;
    private Thread networkThread;

    public MyGame(HashMap<NetworkManager.ConnectionMode, NetworkInterface> networkImpls)
    {
        networkManager = new NetworkManager(networkImpls);
    }

    @Override
    public void create()
    {
        Gdx.input.setInputProcessor(new MyInputProcessor());
        spriteBatch = new SpriteBatch();
        gameStateManager = new GameStateManager(this, networkManager);

        // This must be done in MyGame.create()! putting this in render() will lock up the game.
        networkThread = new Thread(networkManager);
        networkThread.start();
        boolean success = networkManager.initialize(true,
                                                    NetworkManager.ConnectionMode.WIFI_LAN,
                                                    null);

        if (!success)
        {
            System.out.println("NET: Initialize failed.");
        }
    }

    @Override
    public void render()
    {

        // This makes the game run at/close to 60 frams a sec.        side note (1/60 = 0.0166666)
        accumulator += Gdx.graphics.getDeltaTime();
        while (accumulator > step)
        {
        accumulator -= step;
        }

        gameStateManager.update((float)accumulator);
        gameStateManager.render();
        MyInput.update();

        /*
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException e)
        {

        }*/
    }

    public SpriteBatch getSpriteBatch()
    {
        return spriteBatch;
    }

    public OrthographicCamera getCamera()
    {
        return camera;
    }

    public OrthographicCamera getHudCamera()
    {
        return hudCamera;
    }
}
