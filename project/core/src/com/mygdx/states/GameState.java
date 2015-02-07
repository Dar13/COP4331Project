package com.mygdx.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.GameStateManager;

/**
 * Created by James on 2/1/2015.
 */
public abstract class GameState {
    protected GameStateManager gsm;
    protected MyGame game;

    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    protected GameState(GameStateManager gsm){
        this.gsm = gsm;
        game = gsm.getGame();
        sb = game.getSpriteBatch();
        cam = game.getCam();
        hudCam = game.getHudCam();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
}
