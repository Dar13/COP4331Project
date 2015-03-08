package com.mygdx.states;

// LibGDX includes

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by NeilMoore on 2/14/2015.
 */
public class NetTest extends GameState
{
    protected boolean connected = false;
    private Stage stage;
    private TextButton serverButton;
    private TextButton clientButton;

    public NetTest(GameStateManager gameStateManager, final NetworkManager networkManager)
    {
        super(gameStateManager, networkManager);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        serverButton = new TextButton("Server", textButtonStyle);
        serverButton.setPosition(MyGame.V_WIDTH / 4, MyGame.V_HEIGHT * 5 / 8);
        serverButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                networkManager.prepInitialize(true,
                                              NetworkManager.ConnectionMode.WIFI_LAN,
                                              NetworkManager.ConnectionMode.NONE,
                                              true);
            }
        });
        stage.addActor(serverButton);

        clientButton = new TextButton("Client", textButtonStyle);
        clientButton.setPosition(MyGame.V_WIDTH / 4, MyGame.V_HEIGHT / 4);
        clientButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                networkManager.prepInitialize(false,
                                              NetworkManager.ConnectionMode.WIFI_LAN,
                                              NetworkManager.ConnectionMode.NONE,
                                              true);
            }
        });
        stage.addActor(clientButton);
    }

    @Override
    public void handleInput()
    {
    }

    @Override
    public void update(float dt)
    {
    }

    @Override
    public void render()
    {
        // clear screen, then draw stage
        Gdx.gl.glClearColor(0, 0, 0, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void dispose()
    {

    }
}
