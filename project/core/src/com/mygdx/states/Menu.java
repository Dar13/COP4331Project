package com.mygdx.states;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.entities.Actor;
import com.mygdx.game.MyGame;
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
public class Menu extends GameState {

    private MyStage stage;
    private Stage hub;
    private TextButton singleplayer;
    private TextButton multiplayer;
    private TextButton tutorial;
    private TextButton credits;
    private  Actor title;


    public Menu(GameStateManager gameStateManager, NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new MyStage();
        hub = new Stage(new StretchViewport(MyGame.V_WIDTH,MyGame.V_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));

        /* Menu Music */
        AssetManager.loadMusic(1);
        AssetManager.music.play();
        AssetManager.music.setLooping(true);
        AssetManager.loadSound(1);


        title = new Actor(new Texture("Title/title2.png"),-220,-150);
        hub.addActor(title);
        ((OrthographicCamera) hub.getCamera()).zoom =1.54f;

        singleplayer = new TextButton("Single Player",skin);
        singleplayer.setSize(200, 60);
        singleplayer.setPosition(game.V_WIDTH/2- 2.9f*singleplayer.getWidth()/2 , game.V_HEIGHT*6.5f/11);
        singleplayer.addListener(new ClickListener());
        stage.addActor(singleplayer);

        multiplayer = new TextButton("Multiplayer",skin);
        multiplayer.setSize(200, 60);
        multiplayer.setPosition(game.V_WIDTH / 2 - 2.9f*singleplayer.getWidth() / 2, singleplayer.getY() - 65);
        multiplayer.addListener(new ClickListener());
        stage.addActor(multiplayer);

        tutorial = new TextButton("Tutorial",skin);
        tutorial.setSize(200, 60);
        tutorial.setPosition(game.V_WIDTH / 2 - 2.9f*singleplayer.getWidth() / 2, multiplayer.getY() - 65);
        tutorial.addListener(new ClickListener());
        stage.addActor(tutorial);

        credits = new TextButton("Credits",skin);
        credits.setSize(200, 60);
        credits.setPosition(game.V_WIDTH / 2 - 2.9f*singleplayer.getWidth() / 2, tutorial.getY() - 65);
        credits.addListener(new ClickListener());
        stage.addActor(credits);

   }
    @Override
    public void update(float delta)
    {
        if(singleplayer.isChecked())
        {
            networkManager.setSingleplayer(true);
            if(!networkManager.isInitialized())
            {
                networkManager.prepInitialize(true,
                        ConnectionMode.WIFI_LAN,
                        ConnectionMode.NONE,
                        true);
            }
            AssetManager.sound.play();
            gameStateManager.setState(GameStateManager.LEVELSELECT, 0);
        }
        if(multiplayer.isChecked())
        {
            AssetManager.sound.play();
            gameStateManager.setState(GameStateManager.NET, 0);
        }
        if(tutorial.isChecked()){
            AssetManager.sound.play();
            gameStateManager.setState(GameStateManager.TUTORIAL, 0);
        }
        if(credits.isChecked()){
            AssetManager.sound.play();
            gameStateManager.setState(GameStateManager.CREDITS, 0);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, .5f, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        hub.act(delta);
        hub.draw();
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
