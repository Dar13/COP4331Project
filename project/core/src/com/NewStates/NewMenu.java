package com.NewStates;

import com.NewHandlers.NewGameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.handlers.NetworkManager;


/**
 * Created by James on 3/18/2015.
 */
public class NewMenu extends NewGameState{

    private Stage stage;
    private TextButton start;

    public NewMenu(NewGameStateManager gameStateManager,NetworkManager networkManager){
        super(gameStateManager,networkManager);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));
        start = new TextButton("Start",skin);
        start.setSize(200,60);
        start.setPosition(game.V_WIDTH/2-start.getWidth()/2, game.V_HEIGHT/2-start.getHeight()/2);
        start.addListener(new ClickListener());
        stage.addActor(start);
        stage.addActor(start);
   }
    @Override
    public void update() {
        if(start.isChecked()){
            gameStateManager.setState(NewGameStateManager.PLAY);
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
