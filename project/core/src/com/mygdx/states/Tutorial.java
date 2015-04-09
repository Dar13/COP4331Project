package com.mygdx.states;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.mygdx.entities.Actor;
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

import java.awt.TextField;


/**
 * Created by James on 3/18/2015.
 */
public class Tutorial extends GameState {

    private MyStage stage;
    private TextButton Return;
    private TextButton textfield;
    Skin skin;
    int text = 0;
    private String[] tutorialtext = {" Welcome to the Over The Rhine Tutorial.\n Thank you for playing our game. \n To continue, click this dialogue box...\n To return to the Menu, click the Menu button.\n",
                                     " The highlighted area shows your health, gold,\n and the current enemy wave.\n The path that is highlighted represents the path\n the enemies will take.",
                                     " This is a Rifle Squad Bunker, it costs 100 gold.\n The blue circle represents the range.\n The blue square represents the bounds.\n You cannot place any tower on a path.\n",
                                     " The Rile squad has a low attack power, but high\n attack speed. It is ideal for dealing \n with the normal enemy type.\n",
                                     " This is a Bazooka Squad Bunker, it costs 150 gold.\n It has a high attack power, but a slower attack speed\n when compared to the rifle tower.\n It is ideal for dealing with the Tiger Tank.",
                                     " This is the Sniper Bunker, it costs 350 gold.\n It has a higher attack compared to the rifle and\n a faster attack speed when compared to the bazooka.\n It is ideal for dealing with the Fast enemies.",
                                     " This is the Mortar Team Bunker, it costs 350 gold.\n It has an attack speed between the Sniper and the\n bazooka. It's damage is slightly higher than the rifle.\n It can slow the enemies greatly. Ideal for all enemies."
                                    };
    Actor background;
    private Texture Tutorial1 = new Texture("Tutorial1.png");
    private Texture Tutorial2 = new Texture("Tutorial2.png");
    private Texture Tutorial3 = new Texture("Tutorial3.png");
    private Texture Tutorial4 = new Texture("Tutorial4.png");
    private Texture Tutorial5 = new Texture("Tutorial5.png");
    private Texture Tutorial6 = new Texture("Tutorial6.png");
    private Texture[] texturearray = {Tutorial1, Tutorial2, Tutorial3, Tutorial3, Tutorial4, Tutorial5, Tutorial6};
    /*private Texture Tutorial7 = new Texture("Tutorial7.png");
    */
    public Tutorial(final GameStateManager gameStateManager, NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));
        background = new Actor(Tutorial1, 0, 0);
        stage.addActor(background);

        /* Menu Music */
        AssetManager.loadMusic(1);
        //AssetManager.music.play();
        //AssetManager.music.setLooping(true);
        AssetManager.loadSound(1);

        Return = new TextButton("Menu",skin);
        Return.setSize(200, 60);
        Return.setPosition(game.V_WIDTH - 205, 5);
        Return.addListener(new ClickListener());
        stage.addActor(Return);

        textfield = new TextButton(tutorialtext[text], skin);
        textfield.setSize(400, 100);
        textfield.setPosition(5, 5);
        textfield.getLabel().setAlignment(Align.left);
        textfield.addListener(new ClickListener());
        stage.addActor(textfield);
    }
    @Override
    public void update(float delta) {
        if(textfield.isChecked() && text < tutorialtext.length - 1){
            text++;
            textfield.setText(tutorialtext[text]);
            textfield.setChecked(false);
            background.setSprite(texturearray[text]);
        }
        if(Return.isChecked()){
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
