package com.mygdx.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.entities.Actor;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.UI.MyStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.AssetManager;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by LordNeah on 3/22/2015.
 */
public class End extends GameState {
    private MyStage stage;
    private TextButton backtostart;
    private Label label;
    private Texture Map = new Texture("Maps/SubMenuMap.png");

    public End(GameStateManager gameStateManager, NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));

        //Load and Start End Music
        AssetManager.loadMusic(2);
        AssetManager.music.play();
        AssetManager.music.setLooping(true);
        AssetManager.loadSound(3);

        label = new Label("Game Over :(", skin);
        label.setAlignment(1);
        label.setSize(400, 100);
        label.setPosition((game.V_WIDTH - label.getWidth())/2, game.V_HEIGHT - 164);

        backtostart = new TextButton("Return to Menu",skin);
        backtostart.setSize(200,60);
        backtostart.setPosition(game.V_WIDTH/2-backtostart.getWidth()/2, game.V_HEIGHT/2-backtostart.getHeight()/2);
        backtostart.addListener(new ClickListener());

        Actor map = new Actor(Map, 0, 0);
        stage.addActor(map);
        stage.addActor(label);
        stage.addActor(backtostart);
        stage.addActor(backtostart);
    }
    @Override
    public void update(float delta) {
        if(backtostart.isChecked()){
            AssetManager.sound.play();
            networkManager.reset();
            gameStateManager.setState(GameStateManager.MENU, 0);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
