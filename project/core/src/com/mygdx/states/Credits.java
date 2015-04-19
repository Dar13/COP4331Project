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
import com.mygdx.handlers.net.NetworkManager;
import com.mygdx.handlers.WayPointManager;

/**
 * Created by LordNeah on 3/22/2015.
 */
public class Credits extends GameState {
    private MyStage stage;
    private TextButton textField;
    private TextButton toMenu;
    private Texture Map = new Texture("Maps/SubMenuMap.png");
    private WayPointManager wayPointManager;
    private EnemyManager enemyManager;
    private String credittext = "Thank you for playing Over The Rhine.\n From all of us at Anti-Pattern Studios.\n\n\n" +
                                "Kevin Castillo - Gameplay, Balancing, BetaTesting\nTanner Foster - Art, Gameplay, UI\n" +
                                "Kevin Lang - Network, Gameplay\nRobert Lee - Network\nNeil Moore - Network, Gameplay, Optimization\n" +
                                "Miguel Vargas - Gameplay, Sound\nJames Vinson - Gameplay, Art, UI";

    public Credits(GameStateManager gameStateManager, NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));
        Actor map = new Actor(Map, 0, 0);
        stage.addActor(map);
        wayPointManager = new WayPointManager(5);
        enemyManager = new EnemyManager(networkManager,wayPointManager.wayPoints, stage.getBatch());
        enemyManager.LevelSelectWave();
        stage.addActor(enemyManager);

        textField = new TextButton(credittext,skin);
        textField.setSize(400, 300);
        textField.setPosition(game.V_WIDTH/2-textField.getWidth()/2, game.V_HEIGHT - 365);
        textField.setDisabled(true);
        stage.addActor(textField);

        toMenu = new TextButton("Menu", skin);
        toMenu.setSize(200, 60);
        toMenu.setPosition(game.V_WIDTH/2-toMenu.getWidth()/2, textField.getY() - 65);
        toMenu.addListener(new ClickListener());
        stage.addActor(toMenu);


    }
    @Override
    public void update(float delta) {
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
