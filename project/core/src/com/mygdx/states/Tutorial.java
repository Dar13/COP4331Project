package com.mygdx.states;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
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
    Skin skin;
    private Texture Tutorial1 = new Texture("Tutorial1.png");
    int text = 0;
    private String[] tutorialtext = {"Test\n", "Test2\n"};
    /*private Texture Tutorial2 = new Texture("Tutorial2.png");
    private Texture Tutorial3 = new Texture("Tutorial3.png");
    private Texture Tutorial4 = new Texture("Tutorial4.png");
    private Texture Tutorial5 = new Texture("Tutorial5.png");
    private Texture Tutorial6 = new Texture("Tutorial6.png");
    private Texture Tutorial7 = new Texture("Tutorial7.png");
    */
    public Tutorial(final GameStateManager gameStateManager, NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
         skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));

        /* Menu Music */
        AssetManager.loadMusic(1);
        //AssetManager.music.play();
        //AssetManager.music.setLooping(true);
        AssetManager.loadSound(1);

        next = new TextButton("Next",skin);
        next.setSize(200, 60);
        next.setPosition(game.V_WIDTH - 205, 5);
        next.addListener(new ClickListener());
        stage.addActor(next);

        Return = new TextButton("Menu",skin);
        Return.setSize(200, 60);
        Return.setPosition(game.V_WIDTH - 205, game.V_HEIGHT - 65);
        Return.addListener(new ClickListener());
        stage.addActor(Return);

        new Dialog("Tutorial", skin, "dialog") {
            protected void result (Object object) {
                System.out.println("Chosen: " + object);
                if(object.equals(true)) {
                    text++;
                }
                else{
                    gameStateManager.popState();
                }
            }
        }       .text(tutorialtext[text])
                .button("Next", true)
                .button("Menu", false)
                .key(Input.Keys.ENTER, true)
                .key(Input.Keys.ESCAPE, false)
                .show(stage);



    }
    @Override
    public void update(float delta) {

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
