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

    public static final int edgeOffset = 32;
    private boolean debugModeOn = true;
    private Texture TowerImg;
    private Texture TowerShadow;
    private Texture Map;
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
    private LinkedList<Path> paths;

    public Play(GameStateManager gameStateManager, NetworkManager networkManager)
    {
        super(gameStateManager, networkManager);


        paths = new LinkedList<Path>(); // NOTE: what is a path? This is never used. Is this for pathfinding? Tanner: It will be used when placing towers.
        wayPoints = new LinkedList<WayPoint>();
        towers = new LinkedList<Tower>();
        wayPoints.addLast(new WayPoint(0, 0, WayPoint.Direction.EAST));
        wayPoints.addLast(new WayPoint(MyGame.V_WIDTH - edgeOffset, 0, WayPoint.Direction.NORTH));
        wayPoints.addLast(new WayPoint(MyGame.V_WIDTH - edgeOffset, MyGame.V_HEIGHT - edgeOffset, WayPoint.Direction.WEST));
        wayPoints.addLast(new WayPoint(0, MyGame.V_HEIGHT - edgeOffset, WayPoint.Direction.SOUTH));
        wayPoints.addLast(new WayPoint(0, 0, WayPoint.Direction.END));
        enemyManager = new EnemyManager(wayPoints);

        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        Map = new Texture("map.png");
        map = new Sprite(Map);
        TowerImg = new Texture("DevText_Tower.png");
        TowerShadow = new Texture("shadowtower.png");

        // NOTE: need to create concrete tower types to remove magic number constants in code.
        tower = new Tower(TowerImg, TowerShadow, 50, 50, 2, 2);
        towers.addLast(tower);
        towerManager = new TowerManager(towers);
        towerManager.addTower(TowerImg, TowerShadow, 240, 50, 2, 3);
        towerManager.addTower(TowerImg, TowerShadow, 480, 50, 3, 3);
        towerManager.addTower(TowerImg, TowerShadow, 560, 150, 7, 3);
        debugger = new Debugger(wayPoints, towerManager.towers, enemyManager.enemies);

    }

    public void handleInput()
    {
    }

    public void update(float deltaTime)
    {
        enemyManager.Update(deltaTime, towers);
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
