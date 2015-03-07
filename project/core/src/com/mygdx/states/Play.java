package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.Debug.Debugger;
import com.mygdx.UI.GameButton;
import com.mygdx.UI.TowerButton;
import com.mygdx.entities.Tower;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.EnemyManager;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.MyInput;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.handlers.TowerManager;
import com.mygdx.handlers.WayPointManager;
import com.mygdx.triggers.Path;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 2/1/2015.
 */
public class Play extends GameState
{

    public static final int edgeOffset = 32;
    private boolean debugModeOn = true;
    private Texture Map;
    public Sprite map;

    public ShapeRenderer shapeRenderer;
    private OrthographicCamera cam;
    private Debugger debugger;
    private int gold = 0;
    private int towerPlacement = 0;
    private LinkedList<Tower> towers;
    public EnemyManager enemyManager;
    public TowerManager towerManager;
    public WayPointManager wayPointManager;
    private TowerButton rifle;
    private boolean Pause = false;
    private BitmapFont font;
    Texture RifleTower = new Texture("RifleTower.png");
    Texture BazookaTower = new Texture("BazookaTower.png");
    Texture TowerShadow = new Texture("shadowtower.png");
    Sprite towerToBePlaced;
    Sprite towerToBePlacedS;

    public Play(GameStateManager gameStateManager, NetworkManager networkManager)
    {
        super(gameStateManager, networkManager);
        towers = new LinkedList<Tower>();
        wayPointManager = new WayPointManager();
        enemyManager = new EnemyManager(wayPointManager.wayPoints);

        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        Map = new Texture("map.png");

        map = new Sprite(Map);
        rifle = new TowerButton(RifleTower, MyGame.V_WIDTH - 16, MyGame.V_HEIGHT - 16, cam);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.scale(.01f);

        // NOTE: need to create concrete tower types to remove magic number constants in code.
        towerManager = new TowerManager(towers);
        towerManager.addBazookaTower(50, 50);
        towerManager.addRifleTower(240, 50);
        towerManager.addRifleTower(480, 50);
        towerManager.addRifleTower(560, 150);
        debugger = new Debugger(wayPointManager.wayPoints, towerManager.towers, enemyManager.enemies);

    }

    public void handleInput()
    {
    }

    public void update(float fps)
    {
        //Accounting for our future pause state.
        if(!Pause) {
            enemyManager.Update(fps, towers);
            rifle.update(fps);
            if (rifle.clicked && towerPlacement == 0){
                towerToBePlaced = new Sprite(RifleTower);
                towerToBePlacedS = new Sprite(TowerShadow);
                towerToBePlaced.setPosition(MyInput.x, MyInput.y);
                towerToBePlacedS.setPosition(MyInput.x + 9, MyInput.y - 23);
                towerToBePlacedS.rotate(-45);
                towerPlacement = 1;
            }

            else if (MyInput.isDown() && towerPlacement == 1){
                towerToBePlaced.setPosition(MyInput.x, MyInput.y);
                towerToBePlacedS.setPosition(MyInput.x + 9, MyInput.y - 23);
            }

            else if (MyInput.isReleased() && towerPlacement == 1 && !wayPointManager.WithinAny(MyInput.x, MyInput.y)){
              towerManager.addRifleTower(MyInput.x, MyInput.y);
                towerPlacement--;
            }
        }
    }

    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.begin();
        map.draw(spriteBatch);
        rifle.render(spriteBatch);
        if(towerPlacement == 1) {
            towerToBePlaced.draw(spriteBatch);
            towerToBePlacedS.draw(spriteBatch);
        }
        font.draw(spriteBatch, "Rifle", MyGame.V_WIDTH - 32, MyGame.V_HEIGHT - 10);
        enemyManager.RenderAll(spriteBatch);
        towerManager.RenderAll(spriteBatch);

        spriteBatch.end();

        if (debugModeOn)
        {
            debugger.render();
        }
    }

    public void dispose()
    {
    }
}
