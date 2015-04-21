package com.mygdx.states;

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
    private Batch batch;
    private BitmapFont font;

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

        backtostart = new TextButton("Return to Menu",skin);
        backtostart.setSize(200,60);
        backtostart.setPosition(game.V_WIDTH/2-backtostart.getWidth()/2, game.V_HEIGHT/2-backtostart.getHeight()/2);
        backtostart.addListener(new ClickListener());

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.scale(.5f);

        stage.addActor(backtostart);
        stage.addActor(backtostart);
    }
    @Override
    public void update(float delta) {
        if(backtostart.isChecked()){
            AssetManager.sound.play();
            gameStateManager.setState(GameStateManager.MENU, 0);
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
        batch = stage.getBatch();
        batch.begin();
        font.draw(batch, "Game Over :(", MyGame.V_WIDTH / 2 - 48, MyGame.V_HEIGHT - 50);
        batch.end();
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
