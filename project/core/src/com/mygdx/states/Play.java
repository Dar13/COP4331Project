package com.mygdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.Debug.Debugger;
import com.mygdx.entities.Enemy;
import com.mygdx.entities.EnemyHeavy;
import com.mygdx.entities.Tower;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.EnemyManager;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.handlers.TowerManager;
import com.mygdx.triggers.Path;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 2/1/2015.
 */
public class Play extends GameState
{

    public static final int MAGIC_NUMBER = 32;
    private boolean debugModeOn = true;
    private Texture EnemyImg;
    private Texture TigerBase;
    private Texture TigerTurret;
    private Texture TowerImg;
    private Texture Map;
    private Enemy enemy;
    public Sprite map;

    private Tower tower;
    public ShapeRenderer shapeRenderer;
    private OrthographicCamera cam;
    private Debugger debugger;
    private int gold = 0;
    private LinkedList<WayPoint> wayPoints;
    private LinkedList<Tower> towers;
    public EnemyManager enemyManager;
    public TowerManager towerManager;
    float TimeSinceLastSpawn = 0;
    int numNormalEnemies = 0;
    int numHEnemies = 0;
    private LinkedList<Path> paths;

    public Play(GameStateManager gameStateManager, NetworkManager networkManager)
    {
        super(gameStateManager, networkManager);

        enemyManager = new EnemyManager(0, 0);
        paths = new LinkedList<Path>(); // NOTE: what is a path? This is never used. Is this for pathfinding?
        wayPoints = new LinkedList<WayPoint>();
        towers = new LinkedList<Tower>();
        wayPoints.addLast(new WayPoint(0, 0, WayPoint.Direction.EAST));
        wayPoints.addLast(new WayPoint(MyGame.V_WIDTH - MAGIC_NUMBER, 0, WayPoint.Direction.NORTH));
        wayPoints.addLast(new WayPoint(MyGame.V_WIDTH - MAGIC_NUMBER, MyGame.V_HEIGHT - MAGIC_NUMBER, WayPoint.Direction.WEST));
        wayPoints.addLast(new WayPoint(0, MyGame.V_HEIGHT - MAGIC_NUMBER, WayPoint.Direction.SOUTH));
        wayPoints.addLast(new WayPoint(0, 0, WayPoint.Direction.END));


        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        EnemyImg = new Texture("EnemyDev.png");
        TigerBase = new Texture("tigerbase.png");
        TigerTurret = new Texture("tigerturret.png");
        Map = new Texture("map.png");
        map = new Sprite(Map);

        numNormalEnemies = 15;
        numHEnemies = 1;

        enemyManager.AddEnemy(EnemyImg, Enemy.VELOCITY, Enemy.ARMOR, wayPoints);
        enemyManager.AddHeavyEnemy(TigerBase, TigerTurret, EnemyHeavy.VELOCITY, EnemyHeavy.ARMOR, wayPoints);
        TowerImg = new Texture("DevText_Tower.png");

        // NOTE: need to create concrete tower types to remove magic number constants in code.
        tower = new Tower(TowerImg, 50, 50, 2, 2);
        towers.addLast(tower);
        towerManager = new TowerManager(towers);
        towerManager.addTower(TowerImg, 240, 50, 2, 3);
        towerManager.addTower(TowerImg, 480, 50, 3, 3);
        towerManager.addTower(TowerImg, 560, 150, 7, 3);
        debugger = new Debugger(wayPoints, towerManager.towers, enemyManager.enemies);

    }

    public void handleInput()
    {
    }

    public void update(float deltaTime)
    {
        TimeSinceLastSpawn = TimeSinceLastSpawn + deltaTime;

        if (TimeSinceLastSpawn > .5 && numNormalEnemies > 0)
        {
            enemyManager.AddEnemy(EnemyImg, 3, 1, wayPoints);
            TimeSinceLastSpawn = 0;
            numNormalEnemies--;
        }


        enemyManager.UpdateAll(deltaTime, towers);

    }

    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.begin();
        map.draw(spriteBatch);
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
