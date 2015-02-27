package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.net.NetworkInterface;

import java.util.HashMap;

public class MyGame extends ApplicationAdapter
{

    private SpriteBatch spriteBatch;
	private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private GameStateManager gameStateManager;

    private NetworkManager networkManager;
    private Thread networkThread;
    
    public MyGame(HashMap<NetworkManager.ConnectionMode, NetworkInterface> networkImpls)
    {
        networkManager = new NetworkManager(networkImpls);
    }
	
	@Override
	public void create ()
    {
        spriteBatch = new SpriteBatch();
        gameStateManager = new GameStateManager(this, networkManager);

        networkThread = new Thread(networkManager);
        networkThread.start();
        boolean success = networkManager.initialize(true,
                                                    NetworkManager.ConnectionMode.WIFI_LAN,
                                                    null);

        if(!success)
        {
            System.out.println("NET: Initialize failed.");
        }

        /*
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException e)
        {

        }*/
	}

	@Override
	public void render ()
    {
        gameStateManager.update(Gdx.graphics.getDeltaTime());
        gameStateManager.render();
	}

    public SpriteBatch getSpriteBatch(){return spriteBatch;}
    public OrthographicCamera getCamera(){return camera;}
    public OrthographicCamera getHudCamera(){return hudCamera;}
}
