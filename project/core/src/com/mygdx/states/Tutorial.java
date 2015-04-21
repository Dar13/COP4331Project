package com.mygdx.states;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
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
                                     " The highlighted area shows your health, gold,\n and the current enemy wave.\n The blue lines and squares represent the path\n the enemies will take.",
                                     " This is a Rifle Squad Bunker, it costs 100 gold.\n The blue circle represents the range.\n The blue square represents the bounds.\n You cannot place any tower on a path.\n",
                                     " The Rile squad has a low attack power, but high\n attack speed. It is ideal for dealing \n with the normal enemy type.\n",
                                     " This is a Bazooka Squad Bunker, it costs 150 gold.\n It has a high attack power, but a slower attack speed\n when compared to the rifle tower.\n It is ideal for dealing with the Tiger Tank.",
                                     " This is the Sniper Bunker, it costs 350 gold.\n It has a higher attack compared to the rifle and\n a faster attack speed when compared to the bazooka.\n It is ideal for dealing with the Fast enemies.",
                                     " This is the Mortar Team Bunker, it costs 350 gold.\n It has an attack speed between the Sniper and the\n bazooka. It's damage is slightly higher than the rifle.\n It can slow the enemies greatly. Ideal for all enemies.",
                                     " Between every wave you will have a chance to place\n your bunkers. When you are finished press the Ready\n button in the lower right to begin a new wave.",
                                     " To upgrade any tower, simply click or press the\n tower you wish to upgrade and these buttons\n will appear. Each upgrade will cost the base\n cost of the tower times its next level.",
                                     " Now to the enemies.\n This is Klaus, your average Wehrmacht solder.\n He has hopes and dreams.\n But he is a Nazi and must be stopped. Poor Klaus.",
                                     " Like all enemies of his type, Klaus is slow and only\n lightly armoured. A couple of Rifle Bunker Squads\n could deal with Klaus and his friends.",
                                     " This is Hans. Say hello Hans. \n 'hallo' -Hans\n Unlike his brother Klaus, Hans was selected for the\n Waffen SS. That means he is basically a Super Nazi.",
                                     " Hans worked hard to get where he is, which\n means he is faster in the field. However he has the\n same armour as Klaus due to supply shortages.\n A mortar team could slow him down for your Rifles.",
                                     " This is the mighty Tiger I Tank.\n It is commanded by 1st Lt. Bruno.\n Lt. Bruno doesn't say much to the other\n Wehrmacht soldiers, they just wouldn't understand.",
                                     " Lt. Bruno's tank is slow, but heavily armoured.\n It will give our boys a tough time in the field,\n so make sure to have some Bazooka bunkers\n ready to go.",
                                     " Enemy waves in this game are calculated based on\n how many bunkers you have placed, what wave\n you are on, and how much gold you have stored.\n You will need to play smart to survive.",
                                     "-_-_-_-_-_-_-_-_-_-_-_-_-End of Tutorial-_-_-_-_-_-_-_-_-_-_-_-_-_-"
                                    };
    Actor background;
    private Texture Tutorial1 = new Texture("TutorialScreens/Tutorial1.png");
    private Texture Tutorial2 = new Texture("TutorialScreens/Tutorial2.png");
    private Texture Tutorial3 = new Texture("TutorialScreens/Tutorial3.png");
    private Texture Tutorial4 = new Texture("TutorialScreens/Tutorial4.png");
    private Texture Tutorial5 = new Texture("TutorialScreens/Tutorial5.png");
    private Texture Tutorial6 = new Texture("TutorialScreens/Tutorial6.png");
    private Texture Tutorial7 = new Texture("TutorialScreens/Tutorial7.png");
    private Texture Tutorial8 = new Texture("TutorialScreens/Tutorial8.png");
    private Texture Tutorial9 = new Texture("TutorialScreens/Tutorial9.png");
    private Texture Tutorial10 = new Texture("TutorialScreens/Tutorial10.png");
    private Texture[] texturearray = {Tutorial1, Tutorial2, Tutorial3, Tutorial3,
                                      Tutorial4, Tutorial5, Tutorial6, Tutorial1,
                                      Tutorial10,Tutorial7, Tutorial7, Tutorial8,
                                      Tutorial8, Tutorial9, Tutorial9, Tutorial1,
                                      Tutorial1};


    public Tutorial(final GameStateManager gameStateManager, NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));
        background = new Actor(Tutorial1, 0, 0);
        stage.addActor(background);

        AssetManager.loadSound(1);

        Return = new TextButton("Menu",skin);
        Return.setSize(60, 60);
        Return.setPosition(game.V_WIDTH - 130, 5);
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
