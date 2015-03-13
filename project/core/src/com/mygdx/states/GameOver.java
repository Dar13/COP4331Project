package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.UI.GameButton;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.NetworkManager;

/**
 * GameOver state used to bring us back to the menu.
 */
public class GameOver extends GameState {


    public SpriteBatch spriteBatch;
    private OrthographicCamera cam;
    private GameButton Restart;
    private Texture enemyImg;
    private BitmapFont font;



    public GameOver(GameStateManager gameStateManager, NetworkManager networkManager)
    {
        super(gameStateManager, networkManager);

        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);

        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(cam.combined);

        Restart = new GameButton(MyGame.V_WIDTH / 2, MyGame.V_HEIGHT / 2, cam);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.scale(2);
    }

    public void update(float deltaTime)
    {
        Restart.update(deltaTime);
        if (Restart.isReleased())
        {
            gameStateManager.setState(GameStateManager.MENU);
        }
    }

    public void handleInput()
    {
    }

    public void render()
    {
        cam.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Restart.render(spriteBatch);

        spriteBatch.begin();
        font.draw(spriteBatch, "GAME OVER", 210, 430);
        font.draw(spriteBatch, "Back to menu", 200, 275);

        spriteBatch.end();
    }

    public void dispose(){}

}
