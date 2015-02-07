package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.handlers.GameStateManager;

/**
 * Created by James on 2/1/2015.
 */
public class Play extends GameState {
    private Texture img;

    public Play(GameStateManager gsm){
        super(gsm);
        img = new Texture("badlogic.jpg");
    }


    public void handleInput(){};
    public void update(float dt){};
    public void render(){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.begin();
        sb.draw(img, 0, 0);
        sb.end();
    };
    public void dispose(){};
}
