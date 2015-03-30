package com.NewStates;

import com.NewHandlers.NewGameStateManager;
import com.NewUI.MyStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by LordNeah on 3/22/2015.
 */
public class NewLevelSelect extends NewGameState{
    private MyStage stage;
    private TextButton easy;
    private TextButton medium;
    private TextButton hard;
    private TextButton insane;

    public NewLevelSelect(NewGameStateManager gameStateManager,NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));

        easy = new TextButton("Easy",skin);
        easy.setSize(200, 60);
        easy.setPosition(game.V_WIDTH/2-easy.getWidth()/2, game.V_HEIGHT*3/4);
        easy.addListener(new ClickListener());
        stage.addActor(easy);

        medium = new TextButton("Medium",skin);
        medium.setSize(200,60);
        medium.setPosition(game.V_WIDTH / 2 - easy.getWidth() / 2, easy.getY() - 60);
        medium.addListener(new ClickListener());
        stage.addActor(medium);

        hard = new TextButton("Hard",skin);
        hard.setSize(200, 60);
        hard.setPosition(game.V_WIDTH/2-hard.getWidth()/2, medium.getY() - 60);
        hard.addListener(new ClickListener());
        stage.addActor(hard);

        insane = new TextButton("Insane",skin);
        insane.setSize(200, 60);
        insane.setPosition(game.V_WIDTH/2-insane.getWidth()/2, hard.getY() - 60);
        insane.addListener(new ClickListener());
        stage.addActor(insane);
    }
    @Override
    public void update() {
        if(easy.isChecked()){
            gameStateManager.setState(NewGameStateManager.PLAY, 1);
        }
        if(medium.isChecked()) {
            gameStateManager.setState(NewGameStateManager.PLAY, 2);
        }
        if(hard.isChecked()) {
            gameStateManager.setState(NewGameStateManager.PLAY, 3);
        }
        if(insane.isChecked()) {
            gameStateManager.setState(NewGameStateManager.PLAY, 4);
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
