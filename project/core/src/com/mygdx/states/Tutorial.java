package com.mygdx.states;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.UI.MyStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.handlers.AssetManager;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.net.ConnectionMode;


/**
 * Created by James on 3/18/2015.
 */
public class Tutorial extends GameState {

    private MyStage stage;
    private MyStage hub;
    private TextButton next;
    private TextButton Return;


    public Tutorial(GameStateManager gameStateManager, NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));

        /* Menu Music */
        AssetManager.loadMusic(1);
        //AssetManager.music.play();
        //AssetManager.music.setLooping(true);
        AssetManager.loadSound(1);

        next = new TextButton("Next",skin);
        next.setSize(200, 60);
        next.setPosition(game.V_WIDTH - 200, 5);
        next.addListener(new ClickListener());
        stage.addActor(next);

        Return = new TextButton("Menu",skin);
        Return.setSize(200, 60);
        Return.setPosition(game.V_WIDTH - 200, game.V_HEIGHT - 65);
        Return.addListener(new ClickListener());
        stage.addActor(Return);


    }
    @Override
    public void update(float delta) {
        if(next.isPressed()){

        }
        if(Return.isPressed()) {

        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.act(delta);
        stage.draw();
        //((OrthographicCamera)stage.getCamera()).zoom += .01;

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
