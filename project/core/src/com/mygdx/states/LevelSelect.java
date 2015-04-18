package com.mygdx.states;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.entities.Actor;
import com.mygdx.handlers.EnemyManager;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.UI.MyStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.handlers.WayPointManager;

import java.util.List;

/**
 * Created by LordNeah on 3/22/2015.
 */
public class LevelSelect extends GameState {
    private MyStage stage;
    private TextButton easy;
    private TextButton medium;
    private TextButton hard;
    private TextButton insane;
    private TextButton toMenu;
    private Texture Map = new Texture("Maps/SubMenuMap.png");
    private WayPointManager wayPointManager;
    private EnemyManager enemyManager;

    public LevelSelect(GameStateManager gameStateManager, NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));
        Actor map = new Actor(Map, 0, 0);
        stage.addActor(map);
        wayPointManager = new WayPointManager(5);
        //enemyManager = new EnemyManager(networkManager,wayPointManager.wayPoints, stage.getBatch());
        //enemyManager.LevelSelectWave();
        //stage.addActor(enemyManager);

        easy = new TextButton("Easy",skin);
        easy.setSize(200, 60);
        easy.setPosition(game.V_WIDTH/2-easy.getWidth()/2, game.V_HEIGHT * 5/7);
        easy.addListener(new ClickListener());
        stage.addActor(easy);

        medium = new TextButton("Medium",skin);
        medium.setSize(200,60);
        medium.setPosition(game.V_WIDTH / 2 - easy.getWidth() / 2, easy.getY() - 65);
        medium.addListener(new ClickListener());
        stage.addActor(medium);

        hard = new TextButton("Hard",skin);
        hard.setSize(200, 60);
        hard.setPosition(game.V_WIDTH/2-hard.getWidth()/2, medium.getY() - 65);
        hard.addListener(new ClickListener());
        stage.addActor(hard);

        insane = new TextButton("Insane",skin);
        insane.setSize(200, 60);
        insane.setPosition(game.V_WIDTH/2-insane.getWidth()/2, hard.getY() - 65);
        insane.addListener(new ClickListener());
        stage.addActor(insane);

        toMenu = new TextButton("Menu", skin);
        toMenu.setSize(200, 60);
        toMenu.setPosition(game.V_WIDTH/2-toMenu.getWidth()/2, insane.getY() - 65);
        toMenu.addListener(new ClickListener());
        stage.addActor(toMenu);


    }
    @Override
    public void update(float delta) {
        if(easy.isChecked()){
            gameStateManager.setState(GameStateManager.PLAY, 1);
        }
        if(medium.isChecked()) {
            gameStateManager.setState(GameStateManager.PLAY, 2);
        }
        if(hard.isChecked()) {
            gameStateManager.setState(GameStateManager.PLAY, 3);
        }
        if(insane.isChecked()) {
            gameStateManager.setState(GameStateManager.PLAY, 4);
        }
        if(toMenu.isChecked()){
            gameStateManager.setState(GameStateManager.MENU, 0);
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
