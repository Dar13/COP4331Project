package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by James on 2/22/2015.
 */
public class Menu extends GameState
{
    private TextButton start;
    private TextButton net;
    private BitmapFont font;
    private Stage stage;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private TextButtonStyle textButtonStyle;

    public Menu(final GameStateManager gameStateManager, NetworkManager networkManager)
    {
        super(gameStateManager, networkManager);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.scale(3);

        //set up button style
        skin = new Skin();
        buttonAtlas = new TextureAtlas("buttons/buttons.pack");
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button");
        textButtonStyle.down = skin.getDrawable("button_down");

        start = new TextButton("Start", textButtonStyle);
        start.setPosition(MyGame.V_WIDTH / 4, MyGame.V_HEIGHT * 5 / 8);
        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameStateManager.setState(GameStateManager.PLAY);
            }
        });
        stage.addActor(start);

        net = new TextButton("Net", textButtonStyle);
        net.setPosition(MyGame.V_WIDTH / 4, MyGame.V_HEIGHT / 4);
        net.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameStateManager.setState(GameStateManager.NET);
            }
        });
        stage.addActor(net);
    }

    public void handleInput()
    {
    }

    public void update(float deltaTime)
    {
    }

    public void render()
    {
        stage.draw();
    }

    public void dispose()
    {

    }
}
