package com.mygdx.game;

import com.NewHandlers.NewGameStateManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.handlers.MyInputProcessor;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.net.NetworkInterface;

import java.util.HashMap;

public class MyGame extends ApplicationAdapter
{
    public static final int V_WIDTH = 336 * 2 + 32;
    public static final int V_HEIGHT = 256 * 2 +32;
    public static final int SCALE = 2;
    public static final float fpsretrieve = 60f;

    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private NewGameStateManager gameStateManager;
   // private GameStateManager gameStateManager;

    private float fps = 1f/60f;


    private NetworkManager networkManager;
    private Thread networkThread;
    public boolean inAndroid = false;

    public MyGame(HashMap<NetworkManager.ConnectionMode, NetworkInterface> networkImpls, boolean inAndroid)
    {
        networkManager = new NetworkManager(networkImpls);
        this.inAndroid = true;
    }

    @Override
    public void create()
    {
        Gdx.input.setInputProcessor(new MyInputProcessor());
        spriteBatch = new SpriteBatch();
        //gameStateManager = new GameStateManager(this, networkManager, inAndroid);
        gameStateManager = new NewGameStateManager(this, networkManager, inAndroid);

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

        if(Gdx.graphics.getDeltaTime() < fps)
        {
            //System.out.print("DeltaTime: " + Gdx.graphics.getDeltaTime() + " s\n");
            //sleep the difference between our taget fps and time time between frames.
            double sleep = (fps-Gdx.graphics.getDeltaTime())*1000;
            //System.out.print("sleep: " + sleep + " ms\n");
            try
            {
                Thread.sleep((long)(sleep));
            }
            catch (InterruptedException e)
            {
                System.out.print("Error...");
                e.printStackTrace();
            }
        }
        gameStateManager.update();
        gameStateManager.render(fps);
        //gameStateManager.update(fps);
        //gameStateManager.render();

        //MyInput.update();
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

    public boolean GetMachineId(){return inAndroid;}
}