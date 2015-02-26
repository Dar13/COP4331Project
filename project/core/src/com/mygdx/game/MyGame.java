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
    private GameStateManager gsm;

    private NetworkManager networkManager;
    
    public MyGame(HashMap<NetworkManager.ConnectionMode, NetworkInterface> networkImpls)
    {
        networkManager = new NetworkManager();
        networkManager.setNetworkImpls(networkImpls);
    }
	
	@Override
	public void create () {
        spriteBatch = new SpriteBatch();
        gsm = new GameStateManager(this);


	}

	@Override
	public void render () {

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render();



	}

    public SpriteBatch getSpriteBatch(){return spriteBatch;}
    public OrthographicCamera getCamera(){return camera;}
    public OrthographicCamera getHudCamera(){return hudCamera;}
}
