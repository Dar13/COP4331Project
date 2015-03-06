package com.mygdx.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by James on 2/1/2015.
 */
public abstract class GameState
{
    protected GameStateManager gameStateManager;
    protected NetworkManager networkManager;
    protected MyGame game;

    protected SpriteBatch spriteBatch;
    protected OrthographicCamera orthoCamera;
    protected OrthographicCamera hudCamera;

    protected Skin skin;
    protected TextureAtlas buttonAtlas;
    protected TextButton.TextButtonStyle textButtonStyle;
    protected BitmapFont font;

    protected GameState(GameStateManager gameStateManager,
                        NetworkManager networkManager)
    {
        this.gameStateManager = gameStateManager;
        this.networkManager = networkManager;

        game = gameStateManager.getGame();
        spriteBatch = game.getSpriteBatch();
        orthoCamera = game.getCamera();
        hudCamera = game.getHudCamera();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.scale(3);

        // set up button style for use with start and net buttons
        skin = new Skin();
        buttonAtlas = new TextureAtlas("buttons/buttons.pack");
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button");
        textButtonStyle.down = skin.getDrawable("button_down");
    }

    public abstract void handleInput();

    public abstract void update(float deltaTime);

    public abstract void render();

    public abstract void dispose();
}
