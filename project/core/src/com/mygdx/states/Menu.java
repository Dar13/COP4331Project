package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by James on 2/22/2015.
 */
public class Menu extends GameState
{
    private TextButton startButton;
    private TextButton netButton;
    private Stage stage;

    public Menu(final GameStateManager gameStateManager, NetworkManager networkManager)
    {
        super(gameStateManager, networkManager);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        startButton = new TextButton("Start", textButtonStyle);
        startButton.setPosition(MyGame.V_WIDTH / 4, MyGame.V_HEIGHT * 5 / 8);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameStateManager.setState(GameStateManager.PLAY);
            }
        });
        stage.addActor(startButton);

        netButton = new TextButton("Net", textButtonStyle);
        netButton.setPosition(MyGame.V_WIDTH / 4, MyGame.V_HEIGHT / 4);
        netButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameStateManager.setState(GameStateManager.NET);
            }
        });
        stage.addActor(netButton);
    }

    public void handleInput()
    {
    }

    public void update(float deltaTime)
    {
    }

    public void render()
    {
        // clear screen, then draw stage
        Gdx.gl.glClearColor(0, 0, 0, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    public void dispose()
    {

    }
}
