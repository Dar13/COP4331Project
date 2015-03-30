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
    private int gold = 500;
    private int health = 10;
    private int towerPlacement = 0;
    private int Zooka = 0;
    private int Rifle = 0;
    private LinkedList<Tower> towers;
    public EnemyManager enemyManager;
    public TowerManager towerManager;
    public WayPointManager wayPointManager;

    private TowerButton rifle;
    private TowerButton bazooka;
    private boolean Pause = false;
    private BitmapFont font;
    Texture RifleTower = new Texture("RifleTower.png");
    Texture BazookaTower = new Texture("BazookaTower.png");
    Texture TowerShadow = new Texture("shadowtower.png");
    Sprite towerToBePlaced;
    Sprite towerToBePlacedS;

    public Play(GameStateManager gameStateManager, NetworkManager networkManager, int inAndroid)
    {
        super(gameStateManager, networkManager);
        towers = new LinkedList<Tower>();
        wayPointManager = new WayPointManager(inAndroid);
        enemyManager = new EnemyManager(wayPointManager.wayPoints);

        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        Map = new Texture("map.png");

        map = new Sprite(Map);
        rifle = new TowerButton(RifleTower, MyGame.V_WIDTH - 16, MyGame.V_HEIGHT - 16, cam);
        bazooka = new TowerButton(BazookaTower, MyGame.V_WIDTH - 16, MyGame.V_HEIGHT - 64, cam);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.scale(.01f);

        towerManager = new TowerManager(towers);
        //debugger = new Debugger(wayPointManager.wayPoints, towerManager, enemyManager.enemies);

    }

    public void handleInput()
    {
    }

    public void update(float fps)
    {
        //Accounting for our future pause state.
        if(!Pause)
        {
            //Updating enemy manager.
            enemyManager.Update(fps, towers);
            health = health - enemyManager.CheckEnemiesAtEnd();
            gold = gold + (enemyManager.GetDeadEnemies() * 25);
            //Updating the bazooka and rifle placement buttons.
            rifle.update(fps);
            bazooka.update(fps);

            //Creating a rifle sprite to follow the mouse/finger around the screen.
            if (rifle.clicked && towerPlacement == 0 && gold - towerManager.rifleBasePrice >= 0)
            {
                towerToBePlaced = new Sprite(RifleTower);
                towerToBePlacedS = new Sprite(TowerShadow);
                towerToBePlaced.setPosition(MyInput.x, MyGame.V_HEIGHT - MyInput.y);
                towerToBePlacedS.setPosition(MyInput.x + 9,MyGame.V_HEIGHT - MyInput.y - 23);
                towerToBePlacedS.rotate(-45);
                towerPlacement = 1;
                Rifle = 1;
            }

            //Creating a bazooka sprite to follow the mouse/finger around the screen.
            else if (bazooka.clicked && towerPlacement == 0 && gold - towerManager.bazookaBasePrice >= 0)
            {
                towerToBePlaced = new Sprite(BazookaTower);
                towerToBePlacedS = new Sprite(TowerShadow);
                towerToBePlaced.setPosition(MyInput.x, MyGame.V_HEIGHT - MyInput.y);
                towerToBePlacedS.setPosition(MyInput.x + 9, MyGame.V_HEIGHT - MyInput.y - 23);
                towerToBePlacedS.rotate(-45);
                towerPlacement = 1;
                Zooka = 1;
            }

            //Updating sprite location according to mouse location.
            else if (MyInput.isDown() && towerPlacement == 1)
            {
                towerToBePlaced.setPosition(MyInput.x, MyGame.V_HEIGHT - MyInput.y);
                towerToBePlacedS.setPosition(MyInput.x + 9, MyGame.V_HEIGHT - MyInput.y - 23);
            }

            //Placing a tower and adding to the linked list.
            else if (MyInput.isReleased() && towerPlacement == 1 && !wayPointManager.WithinAny(MyInput.x, MyInput.y))
            {
                if(Rifle == 1)
                {
                    towerManager.addRifleTower(MyInput.x, MyGame.V_HEIGHT - MyInput.y);
                    towerPlacement--;
                    Rifle--;
                    gold = gold - towerManager.rifleBasePrice;
                }

                else if(Zooka == 1)
                {
                    towerManager.addBazookaTower(MyInput.x, MyGame.V_HEIGHT - MyInput.y);
                    towerPlacement--;
                    Zooka--;
                    gold = gold - towerManager.bazookaBasePrice;
                }
            }

            //If player health is zero, switch to LOSE gamestate.
            if(health <= 0)
            {
                gameStateManager.setState(GameStateManager.LOSE);
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
        bazooka.render(spriteBatch);
        if(towerPlacement == 1)
        {
            towerToBePlacedS.draw(spriteBatch);
            towerToBePlaced.draw(spriteBatch);
        }
        font.draw(spriteBatch, "Rifle", MyGame.V_WIDTH - 32, MyGame.V_HEIGHT - 10);
        font.draw(spriteBatch, "Bazooka", MyGame.V_WIDTH - 32, MyGame.V_HEIGHT - 55);
        font.draw(spriteBatch, "Health: " + health, 0, MyGame.V_HEIGHT - 10);
        font.draw(spriteBatch, "Gold: " + gold, 96, MyGame.V_HEIGHT - 10);
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