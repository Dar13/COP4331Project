package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    public static final float fps = 60f;

    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private GameStateManager gameStateManager;

    private double accumulator;


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

        if(Gdx.graphics.getDeltaTime() < 1f/fps)
        {
            System.out.print("DeltaTime: " + Gdx.graphics.getDeltaTime() + " s\n");
            float sleep = (1000/fps-Gdx.graphics.getDeltaTime());
            System.out.print("sleep: " + sleep + " ms\n");
            try
            {
                Thread.sleep((long)(1000/fps-Gdx.graphics.getDeltaTime()));
            }
            catch (InterruptedException e)
            {
                System.out.print("Error...");
                e.printStackTrace();
            }
        }

        gameStateManager.update(Gdx.graphics.getDeltaTime());
        gameStateManager.render();
        MyInput.update();
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
