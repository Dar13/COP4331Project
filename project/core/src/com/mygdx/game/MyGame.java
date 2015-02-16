package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.handlers.GameStateManager;

public class MyGame extends ApplicationAdapter {
    public static final int V_WIDTH = 320;
    public static final int V_HEIGHT = 240;
    public static final int SCALE = 2;

    private SpriteBatch sb;
	private OrthographicCamera cam;
    private OrthographicCamera hudCam;
    private GameStateManager gsm;


	
	@Override
	public void create () {
        sb = new SpriteBatch();
        gsm = new GameStateManager(this);
	}

	@Override
	public void render () {

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render();

	}

    public SpriteBatch getSpriteBatch(){return sb;}
    public OrthographicCamera getCam(){return cam;}
    public OrthographicCamera getHudCam(){return hudCam;}
}
