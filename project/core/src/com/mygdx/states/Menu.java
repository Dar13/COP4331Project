package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.UI.GameButton;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.MyInput;
import com.mygdx.handlers.NetworkManager;

/**
 * Created by James on 2/22/2015.
 */
public class Menu extends GameState {

    public SpriteBatch spriteBatch;
    private OrthographicCamera cam;
    private GameButton start;
    private Texture EnemyImg;
    private BitmapFont font;

    public Menu(GameStateManager gameStateManager, NetworkManager networkManager)
    {
        super(gameStateManager, networkManager);
        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGame.V_WIDTH,MyGame.V_HEIGHT);
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(cam.combined);
        start = new GameButton(MyGame.V_WIDTH/2,MyGame.V_HEIGHT/2,cam);
        EnemyImg = new Texture("EnemyDev.png");
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.scale(3);
    }

    public void handleInput(){}

    public void update(float deltaTime){
        start.update(deltaTime);
        if( start.isReleased()){
            gameStateManager.setState(GameStateManager.PLAY);
        }
    }

    public void render()
    {
        cam.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        start.render(spriteBatch);
        spriteBatch.begin();
        font.draw(spriteBatch,"Start",260,270);
        spriteBatch.end();
    }

    public void dispose(){}
}
