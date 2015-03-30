package com.NewStates;

import com.NewHandlers.NewGameStateManager;
import com.NewUI.MyStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.handlers.AssetManager;
import com.mygdx.handlers.NetworkManager;


/**
 * Created by James on 3/18/2015.
 */
public class NewMenu extends NewGameState{

    private MyStage stage;
    private TextButton singleplayer;
    private TextButton multiplayer;

    public NewMenu(NewGameStateManager gameStateManager,NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));

        /* Menu Music */
        AssetManager.loadMusic(1);
        AssetManager.music.play();
        AssetManager.loadSound(1);

        singleplayer = new TextButton("Single Player",skin);
        singleplayer.setSize(200, 60);
        singleplayer.setPosition(game.V_WIDTH/2- singleplayer.getWidth()/2, game.V_HEIGHT*3/4);
        singleplayer.addListener(new ClickListener());
        stage.addActor(singleplayer);

        multiplayer = new TextButton("Multiplayer",skin);
        multiplayer.setSize(200, 60);
        multiplayer.setPosition(game.V_WIDTH / 2 - singleplayer.getWidth() / 2, game.V_HEIGHT / 4);
        multiplayer.addListener(new ClickListener());
        stage.addActor(multiplayer);
   }
    @Override
    public void update() {
        if(singleplayer.isChecked()){
            gameStateManager.setState(NewGameStateManager.LEVELSELECT, 0);
            AssetManager.sound.play();
        }
        if(multiplayer.isChecked()) {
            gameStateManager.setState(NewGameStateManager.NET, 0);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update();
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
