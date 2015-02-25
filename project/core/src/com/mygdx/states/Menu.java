package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.UI.GameButton;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.GameStateManager;

/**
 * Created by James on 2/22/2015.
 */
public class Menu extends GameState {

    public SpriteBatch spriteBatch;
    private OrthographicCamera cam;
    private GameButton start;
    private Texture EnemyImg;

    public Menu(GameStateManager gsm)
    {
        super(gsm);
        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGame.V_WIDTH,MyGame.V_HEIGHT);
        spriteBatch = new SpriteBatch();
        start = new GameButton(0,0,cam);
        EnemyImg = new Texture("EnemyDev.png");
    }

    public void handleInput(){}

    public void update(float deltaTime){}

    public void render()
    {
        cam.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //spriteBatch.setProjectionMatrix(cam.combined);
        //spriteBatch.begin();
        start.render(spriteBatch);
        //spriteBatch.draw(EnemyImg,0,0);
        //spriteBatch.end();


    }

    public void dispose(){}
}
