package com.mygdx.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.entities.Actor;
import com.mygdx.handlers.AssetManager;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.UI.MyStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by LordNeah on 3/22/2015.
 */
public class WinState extends GameState {
    private MyStage stage;
    private TextButton backtostart;
    private TextButton endlessReplay;
    private Label label;
    private Texture Map = new Texture("Maps/SubMenuMap.png");

    public WinState(GameStateManager gameStateManager, NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));

        //Load and Start Win Music
        AssetManager.loadMusic(3);
        AssetManager.music.play();
        AssetManager.music.setLooping(true);
        AssetManager.loadSound(3);

        label = new Label("Congrats a Winner is you.", skin);
        label.setAlignment(1);
        label.setSize(400, 100);
        label.setPosition((game.V_WIDTH - label.getWidth())/2, game.V_HEIGHT - 164);

        backtostart = new TextButton("Return to Menu",skin);
        backtostart.setSize(200,60);
        backtostart.setPosition(game.V_WIDTH/2-backtostart.getWidth()/2, game.V_HEIGHT/2-backtostart.getHeight()/2);
        backtostart.addListener(new ClickListener());

        endlessReplay = new TextButton("Endless Mode.",skin);
        endlessReplay.setSize(200,60);
        endlessReplay.setPosition(game.V_WIDTH/2-backtostart.getWidth()/2, backtostart.getY() - 65);
        endlessReplay.addListener(new ClickListener());

        Actor map = new Actor(Map, 0, 0);
        stage.addActor(map);
        stage.addActor(label);
        stage.addActor(backtostart);
        stage.addActor(endlessReplay);
    }
    @Override
    public void update(float delta) {
        stage.act(delta);
        if(backtostart.isChecked()){
            AssetManager.sound.play();
            networkManager.reset();
            gameStateManager.setState(GameStateManager.MENU, 0);
        }
        if(endlessReplay.isChecked()){
            AssetManager.sound.play();
            gameStateManager.popState();
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
        stage.draw();
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
