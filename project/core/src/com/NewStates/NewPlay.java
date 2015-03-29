package com.NewStates;


import com.NewEntities.Actor;
import com.NewEntities.BazookaTower;

import com.NewEntities.Enemy;
import com.NewEntities.Entity;

import com.NewEntities.MortarTower;
import com.NewEntities.RifleTower;
import com.NewEntities.SniperTower;
import com.NewEntities.Tower;
import com.NewHandlers.NewEnemyManager;
import com.NewHandlers.NewGameStateManager;
import com.NewHandlers.NewTowerManager;
import com.NewUI.MyStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.Debug.Debugger;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.MyInput;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.handlers.WayPointManager;


import java.util.LinkedList;
import java.util.List;


/**
 * Created by James on 3/18/2015.
 */
public class NewPlay extends  NewGameState {

    private int gold = 500;
    private int health = 10;
    private int towerPlacement = 0;
    private int Zooka = 0;
    private int Rifle = 0;
    private int sniper = 0;
    private int mortar = 0;

    private boolean clicked = false;
    private boolean debuggerOn = true;

    //private LinkedList<NewTower> towers;
    private  Actor map;
    private List<Tower> towers;
    public NewEnemyManager enemyManager;
    public NewTowerManager towerManager;
    public WayPointManager wayPointManager;

    private TextButton rifleButton;
    private TextButton bazookaButton;
    private TextButton sniperButton;
    private TextButton mortarButton;
    private TextButton readyButton;


    private MyStage stage;
    private Texture mapImg;
    private Debugger debugger;

    private OrthographicCamera camera;

    Sprite towerToBePlaced;
    Sprite towerToBePlacedS;

    Texture rifleTowerTexture;
    Texture bazookaTowerTexture;
    Texture sniperTowerTexture;
    Texture mortarTowerTexture;
    Texture towerShadowTexture;

    private BitmapFont font;
    private Batch batch;
    public ShapeRenderer shapeRenderer;

    public enum SubState
    {
        SETUP,
        PLAY,
    }
    private SubState state;

    public NewPlay(NewGameStateManager gameStateManager,NetworkManager networkManager, boolean inAndroid)
    {
        super(gameStateManager,networkManager);
        state = SubState.SETUP;
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));
        mapImg = new Texture("MapEasy.png");
        map = new Actor(mapImg,0,0);

        // load textures
        rifleTowerTexture = new Texture(NewTowerManager.rifleTexturePath);
        bazookaTowerTexture = new Texture(NewTowerManager.bazookaTexturePath);
        sniperTowerTexture  = new Texture(NewTowerManager.sniperTexturePath);
        mortarTowerTexture  = new Texture(NewTowerManager.mortarTexturePath);
        towerShadowTexture = new Texture(NewTowerManager.shadowTexturePath);

        ((OrthographicCamera)stage.getCamera()).position.set(MyGame.V_WIDTH/2,MyGame.V_HEIGHT/2,0f);//setToOrtho(false,100,200);  //.zoom += .01;
        stage.getBatch().setProjectionMatrix(stage.getCamera().combined);

        shapeRenderer = new ShapeRenderer();


        towers = new LinkedList<>();
        wayPointManager = new WayPointManager(inAndroid);
        enemyManager = new NewEnemyManager(wayPointManager.wayPoints, stage.getBatch());
        towerManager = new NewTowerManager(towers);

        rifleButton = new TextButton("rifle",skin);
        rifleButton.setSize(64, 64);
        rifleButton.setPosition(game.V_WIDTH - rifleButton.getWidth(),
                                game.V_HEIGHT - rifleButton.getHeight());
        rifleButton.addListener(new ClickListener());

        bazookaButton = new TextButton("bazooka",skin);
        bazookaButton.setSize(64, 64);
        bazookaButton.setPosition(rifleButton.getX(),rifleButton.getY() - 64);
        bazookaButton.addListener(new ClickListener());


        readyButton = new TextButton("Ready",skin);
        readyButton.setSize(64,64);
        readyButton.setPosition(game.V_WIDTH- readyButton.getWidth(),
                                0);
        readyButton.addListener(new ClickListener());

        sniperButton = new TextButton("sniper",skin);
        sniperButton.setSize(64, 64);
        sniperButton.setPosition(bazookaButton.getX(),bazookaButton.getY() - 64);
        sniperButton.addListener(new ClickListener());

        mortarButton = new TextButton("mortar",skin);
        mortarButton.setSize(64, 64);
        mortarButton.setPosition(sniperButton.getX(),sniperButton.getY() - 64);
        mortarButton.addListener(new ClickListener());


        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.scale(.01f);


        stage.addActor(map);
        stage.addActor(enemyManager);
        stage.addActor(towerManager);
        stage.addActor(rifleButton);
        stage.addActor(bazookaButton);
        stage.addActor(sniperButton);
        stage.addActor(mortarButton);
        stage.addActor(readyButton);




        debugger = new Debugger(wayPointManager.wayPoints, towerManager.towerList, enemyManager.enemyList, stage.getBatch());

    }

    @Override
    public void update() {
        //((OrthographicCamera)stage.getCamera()).zoom +=.001f;
        health = health - enemyManager.CheckEnemiesAtEnd();
        gold = gold + (enemyManager.GetDeadEnemies() * 15);

        placeATower();
        if(readyButton.isPressed()){
            state = SubState.PLAY;
            readyButton.remove();
        }
        if(health <= 0)
        {
            gameStateManager.setState(NewGameStateManager.BADEND);
        }
        else if (enemyManager.currentWave == 10 && (enemyManager.waveToBeSpawnedFast + enemyManager.waveToBeSpawnedNorm + enemyManager.waveToBeSpawnedHeavy) == 0)
        {
            gameStateManager.setState(NewGameStateManager.MENU);
        }


    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        switch (state){
            case SETUP:
                batch = stage.getBatch();
                batch.begin();
                map.draw(batch, 1);
                rifleButton.act(delta);
                bazookaButton.act(delta);
                sniperButton.act(delta);
                mortarButton.act(delta);
                readyButton.act(delta);
                batch.end();
                if(towerPlacement == 1)
                {
                    // Shape renderer is so heavy it will prevent anything else being drawn
                    // if it is in the same batch as it.
                    batch.begin();
                    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(Color.CYAN);
                    shapeRenderer.rect(towerToBePlaced.getX(), towerToBePlaced.getY(), 32, 32);
                    if (Rifle == 1){
                        shapeRenderer.circle(towerToBePlaced.getX() + 16, towerToBePlaced.getY() + 16, RifleTower.BASE_RANGE * 32);
                    }
                    else if (Zooka == 1){
                        shapeRenderer.circle(towerToBePlaced.getX() + 16, towerToBePlaced.getY() + 16, BazookaTower.BASE_RANGE * 32);
                    }
                    else if (sniper == 1){
                        shapeRenderer.circle(towerToBePlaced.getX() + 16, towerToBePlaced.getY() + 16, SniperTower.BASE_RANGE * 32);
                    }
                    else if (mortar == 1){
                        shapeRenderer.circle(towerToBePlaced.getX() + 16, towerToBePlaced.getY() + 16, MortarTower.BASE_RANGE * 32);
                    }
                    shapeRenderer.end();
                    batch.end();
                    batch.begin();
                    towerToBePlaced.draw(batch);
                    batch.end();
                }
                batch.begin();
                font.draw(batch, "Health: " + health, 0, MyGame.V_HEIGHT - 10);
                font.draw(batch, "Gold: " + gold, 96, MyGame.V_HEIGHT - 10);
                font.draw(batch, "Wave: " + enemyManager.currentWave, 192, MyGame.V_HEIGHT - 10);
                debugger.setBatch(batch);
                rifleButton.draw(batch, 1);
                bazookaButton.draw(batch,1);
                sniperButton.draw(batch,1);
                mortarButton.draw(batch,1);
                readyButton.draw(batch,1);
                towerManager.draw(batch,1);
                batch.end();
                break;
            case PLAY:
                enemyManager.SetTowers(towers);
                stage.act(delta);
                stage.draw();
                batch = stage.getBatch();
                batch.begin();
                if(towerPlacement == 1)
                {
                    towerToBePlaced.draw(batch);
                }
                font.draw(batch, "Health: " + health, 0, MyGame.V_HEIGHT - 10);
                font.draw(batch, "Gold: " + gold, 96, MyGame.V_HEIGHT - 10);
                font.draw(batch, "Wave: " + enemyManager.currentWave, 192, MyGame.V_HEIGHT - 10);
                batch.end();
                for(Enemy enemy : enemyManager.enemyList)
                {
                    if(enemy != null)
                    {
                        for(Tower tower : towerManager.towerList)
                        {
                            if(tower.inRange(enemy.getPosition(), enemyManager.centerOffset, enemyManager.rangeOffset) &&
                                    tower.readyToFire() &&
                                    enemy.entityID == tower.getTarget())
                            {
                                shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
                                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                                shapeRenderer.setColor(Color.RED);
                                Vector2 towerPosition = tower.getPosition();
                                Vector2 enemyPosition = enemy.getPosition();
                                shapeRenderer.line(towerPosition.x + enemyManager.centerOffset, towerPosition.y + enemyManager.centerOffset, enemyPosition.x + enemyManager.centerOffset, enemyPosition.y + enemyManager.centerOffset);
                                shapeRenderer.end();
                            }
                        }
                    }
                }


                debugger.setBatch(batch);
                if(debuggerOn) {
                    debugger.render();
                }
                break;
        }

    }

    public void placeATower(){
        boolean clearedForPlacement = true;
        if(rifleButton.isPressed() && towerPlacement == 0 &&
                gold >= RifleTower.PRICE){
            System.out.println("test - rifle button");
            towerToBePlaced = new Sprite(rifleTowerTexture);
            towerToBePlacedS = new Sprite(towerShadowTexture);
            towerToBePlaced.setPosition(MyInput.x ,MyInput.y);
            towerPlacement = 1;
            Rifle = 1;
        }

        // bazooka button handling
        if(bazookaButton.isPressed() && towerPlacement==0 &&
                gold >= BazookaTower.PRICE){
            System.out.println("test - bazooka button");
            towerToBePlaced = new Sprite(bazookaTowerTexture);
            towerToBePlacedS = new Sprite(towerShadowTexture);
            towerToBePlaced.setPosition(MyInput.x, MyInput.y);
            towerPlacement = 1;
            Zooka = 1;
        }

        if(sniperButton.isPressed() && towerPlacement==0 &&
                gold >= SniperTower.PRICE){
            System.out.println("test - sniper button");
            towerToBePlaced = new Sprite(sniperTowerTexture);
            towerToBePlacedS = new Sprite(towerShadowTexture);
            towerToBePlaced.setPosition(MyInput.x, MyInput.y);
            towerPlacement = 1;
            sniper = 1;
        }

        if(mortarButton.isPressed() && towerPlacement==0 &&
                gold >= MortarTower.PRICE){
            System.out.println("test - mortar button");
            towerToBePlaced = new Sprite(mortarTowerTexture);
            towerToBePlacedS = new Sprite(towerShadowTexture);
            towerToBePlaced.setPosition(MyInput.x, MyInput.y);
            towerPlacement = 1;
            mortar = 1;
        }

        if(towerPlacement == 1) {
            if (wayPointManager.WithinAny(towerToBePlaced.getX(), towerToBePlaced.getY()) || towerManager.onAnotherTower(towerToBePlaced.getX(), towerToBePlaced.getY())) {
                clearedForPlacement = false;
            } else if (towerToBePlaced.getX() > 640 || towerToBePlaced.getY() > 480 || towerToBePlaced.getX() + 32 > 640 || towerToBePlaced.getY() + 32 > 480 || towerToBePlaced.getY() < 0) {
                clearedForPlacement = false;
            }

        }

        if (!Gdx.input.isTouched() && towerPlacement == 1 && !clearedForPlacement)
        {
            towerPlacement = 0;
            Rifle = 0;
            Zooka = 0;
        }

        if (!Gdx.input.isTouched() && towerPlacement == 1 && clearedForPlacement)
        {
            Vector2 stageCoordinates = stage.screenToStageCoordinates(new Vector2(MyInput.x, MyInput.y));
            System.out.println(stageCoordinates);
            if(Rifle == 1)
            {
                /*
                towerManager.addRifleTower(stage.screenToStageCoordinates(new Vector2(MyInput.x,MyInput.y)).x,
                                           stage.screenToStageCoordinates(new Vector2(MyInput.x,MyInput.y)).y);
                                           */
                towerManager.addTower(Tower.Type.TOWER_RIFLE, MyInput.x, MyInput.y);
                towerPlacement--;
                Rifle--;
                gold = gold - RifleTower.PRICE;
            }

            else if(Zooka == 1)
            {
                towerManager.addTower(Tower.Type.TOWER_BAZOOKA, MyInput.x, MyInput.y);
                towerPlacement--;
                Zooka--;
                gold = gold - BazookaTower.PRICE;
            }

            else if (sniper == 1)
            {
                towerManager.addTower(Tower.Type.TOWER_SNIPER, MyInput.x, MyInput.y);
                towerPlacement--;
                sniper--;
                gold = gold - SniperTower.PRICE;
            }

            else if (mortar == 1)
            {
                towerManager.addTower(Tower.Type.TOWER_MORTAR, MyInput.x, MyInput.y);
                towerPlacement--;
                mortar--;
                gold = gold - MortarTower.PRICE;
            }
        }

        if (towerPlacement == 1)
        {
            towerToBePlaced.setPosition(MyInput.x - 16,MyInput.y - 16);
        }
        else if(Gdx.input.isTouched())
        {
            switch (towerPlacement){
                case (1):
                    towerPlacement = 0;
                    break;
            }
        }

        if(health <= 0)
        {
            gameStateManager.setState(NewGameStateManager.BADEND);
        }
        else if (enemyManager.currentWave == 10 && (enemyManager.waveToBeSpawnedFast + enemyManager.waveToBeSpawnedNorm + enemyManager.waveToBeSpawnedHeavy) == 0)
        {
            gameStateManager.setState(NewGameStateManager.MENU);
        }


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
