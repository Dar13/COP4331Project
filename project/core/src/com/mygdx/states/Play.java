package com.mygdx.states;


import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.entities.Actor;
import com.mygdx.entities.BazookaTower;

import com.mygdx.entities.Enemy;

import com.mygdx.entities.MortarTower;
import com.mygdx.entities.RifleTower;
import com.mygdx.entities.SniperTower;
import com.mygdx.entities.Tower;
import com.mygdx.handlers.EnemyManager;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.handlers.TowerManager;
import com.mygdx.UI.MyStage;
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
import com.mygdx.handlers.WaveCalculator;
import com.mygdx.handlers.WayPointManager;
import com.mygdx.handlers.action.Action;
import com.mygdx.handlers.action.ActionCreateWave;
import com.mygdx.handlers.action.ActionEnemyCreate;
import com.mygdx.handlers.action.ActionHealthChanged;
import com.mygdx.handlers.action.ActionPlayerWaveReady;
import com.mygdx.handlers.action.ActionTowerPlaced;


import java.util.LinkedList;
import java.util.List;


/**
 * Created by James on 3/18/2015.
 */
public class Play extends GameState {

    private int gold = 500;
    private int health = 10;
    private int towerPlacement = 0;
    private int Zooka = 0;
    private int Rifle = 0;
    private int sniper = 0;
    private int mortar = 0;
    private int totalWaves = 0;

    private boolean clicked = false;
    private boolean debuggerOn = false;

    //private LinkedList<NewTower> towers;
    private  Actor map;
    private List<Tower> towers;
    public EnemyManager enemyManager;
    public TowerManager towerManager;
    public WayPointManager wayPointManager;

    private TextButton rifleButton;
    private TextButton bazookaButton;
    private TextButton sniperButton;
    private TextButton mortarButton;
    private TextButton readyButton;


    private MyStage stage;
    private Stage hub;
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

    private WaveCalculator waveCalculator;

    public enum SubState
    {
        SETUP,
        PLAY,
        PAUSED,
    }
    private SubState state;

    public Play(GameStateManager gameStateManager, final NetworkManager networkManager, int MapLoad)
    {
        super(gameStateManager,networkManager);
        state = SubState.SETUP;
        stage = new MyStage();
        hub = new Stage(new StretchViewport(MyGame.V_WIDTH,MyGame.V_HEIGHT));
        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(stage);
        im.addProcessor(hub);
        Gdx.input.setInputProcessor(im);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));
        switch (MapLoad){
            case 1:
                mapImg = new Texture("Maps/MapEasy.png");
                totalWaves = 20;
                break;
            case 2:
                mapImg = new Texture("Maps/Slightlyhardermap.png");
                totalWaves = 30;
                break;
            case 3:
                mapImg = new Texture("Maps/MapMoreHarder.png");
                totalWaves = 40;
                break;
            case 4:
                mapImg = new Texture("Maps/mapinsane.png");
                totalWaves = 50;
                gold += 200;
                break;
        }

        map = new Actor(mapImg,0,0);

        // load textures
        rifleTowerTexture = new Texture(TowerManager.rifleTexturePath);
        bazookaTowerTexture = new Texture(TowerManager.bazookaTexturePath);
        sniperTowerTexture  = new Texture(TowerManager.sniperTexturePath);
        mortarTowerTexture  = new Texture(TowerManager.mortarTexturePath);
        towerShadowTexture = new Texture(TowerManager.shadowTexturePath);

        ((OrthographicCamera)stage.getCamera()).position.set(MyGame.V_WIDTH / 2, MyGame.V_HEIGHT / 2, 0f);//setToOrtho(false,100,200);  //.zoom += .01;
        stage.getBatch().setProjectionMatrix(stage.getCamera().combined);

        shapeRenderer = new ShapeRenderer();


        towers = new LinkedList<>();
        wayPointManager = new WayPointManager(MapLoad);
        towerManager = new TowerManager(towers);

        rifleButton = new TextButton("rifle",skin);
        rifleButton.setSize(64, 64);
        rifleButton.setPosition(game.V_WIDTH - rifleButton.getWidth(),
                game.V_HEIGHT - rifleButton.getHeight());
        rifleButton.addListener(new ClickListener());

        bazookaButton = new TextButton("bazooka",skin);
        bazookaButton.setSize(64, 64);
        bazookaButton.setPosition(rifleButton.getX(), rifleButton.getY() - 64);
        bazookaButton.addListener(new ClickListener());


        readyButton = new TextButton("Ready",skin);
        readyButton.setSize(64, 64);
        readyButton.setPosition(game.V_WIDTH - readyButton.getWidth(),
                0);
        readyButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                state = SubState.PLAY;
                readyButton.setVisible(false);
                readyButton.setDisabled(true);

                networkManager.addToSendQueue(new ActionPlayerWaveReady());
            }
        });

        sniperButton = new TextButton("sniper",skin);
        sniperButton.setSize(64, 64);
        sniperButton.setPosition(bazookaButton.getX(), bazookaButton.getY() - 64);
        sniperButton.addListener(new ClickListener());

        mortarButton = new TextButton("mortar",skin);
        mortarButton.setSize(64, 64);
        mortarButton.setPosition(sniperButton.getX(), sniperButton.getY() - 64);
        mortarButton.addListener(new ClickListener());


        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.scale(.01f);


        stage.addActor(map);
        stage.addActor(towerManager);
        hub.addActor(rifleButton);
        hub.addActor(bazookaButton);
        hub.addActor(sniperButton);
        hub.addActor(mortarButton);
        hub.addActor(readyButton);

        debugger = new Debugger(wayPointManager.wayPoints, towerManager.towerList, stage.getBatch());

    }

    @Override
    public void update(float delta)
    {
        handleChanges();

        /**
         * TODO
         * Similarly with getting the amount of gold, depending on how
         * we implement resource management. We would presumably send the
         * amount of gold we got this update, and the server would return
         * the global amount of gold.
         */

        placeATower();
        if(readyButton.isPressed()){
            //state = SubState.PLAY;
            //readyButton.setVisible(false);
            //readyButton.setDisabled(true);
            //networkManager.addToSendQueue(new ActionPlayerWaveReady());
        }

        switch (state) {
            case SETUP:
                hub.act(delta);
                stage.act(delta);
                gold -= towerManager.towerAct(delta, gold);
                break;
            case PAUSED:
                hub.act(delta);
                stage.act(delta);
                gold -= towerManager.towerAct(delta, gold);
                break;
            case PLAY:
                if(enemyManager == null)
                {
                    enemyManager = new EnemyManager(networkManager, wayPointManager.wayPoints, stage.getBatch());
                    stage.addActor(enemyManager);
                }
                if(enemyManager.isPaused())
                {
                    if(enemyManager.currentWave == totalWaves)
                    {
                        // TODO: send this info to the Server so all clients end. Also move alot of the if-statement logic to the server?
                        // Maybe server indeed needs to count how many enemy killed in order to send this action
                        gameStateManager.pushState(GameStateManager.GOODEND, 0);
                    }

                    state = SubState.PAUSED;
                    readyButton.setVisible(true);
                    readyButton.setDisabled(false);
                }
                stage.act(delta);
                hub.act(delta);
                gold -= towerManager.towerAct(delta, gold);
                gold = gold + (enemyManager.GetGoldEarned());

                if(health <= 0)
                {
                    gameStateManager.setState(GameStateManager.BADEND, 0);
                }
                
                break;

        }



    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, .5f, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      // ((OrthographicCamera)stage.getCamera()).zoom += 0.001;
        switch (state){
            case SETUP:
                hub.draw();
                stage.draw();
                batch = stage.getBatch();
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
                font.draw(batch, "Wave: 0", 192, MyGame.V_HEIGHT - 10);
                debugger.setBatch(batch);
                //towerManager.draw(batch, 1);
                batch.end();
                break;
            case PAUSED:
                hub.draw();
                stage.draw();
                batch = stage.getBatch();
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
                //towerManager.draw(batch, 1);
                batch.end();
                break;
            case PLAY:
                enemyManager.SetTowers(towers);
                stage.draw();
                hub.draw();
                enemyManager.setGold(gold);
                batch = stage.getBatch();
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
                                shapeRenderer.setColor(Color.WHITE);
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

    @Override
    public void resize(int width, int height)
    {
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
            towerToBePlaced.setPosition(MyInput.x - 16, MyInput.y - 16);
        }
        else if(Gdx.input.isTouched())
        {
            switch (towerPlacement){
                case (1):
                    towerPlacement = 0;
                    break;
            }
        }


    }

    public void decrimentGold(int sub)
    {
        gold = gold - sub;
    }

    public void handleChanges()
    {
        List<Action> changes = networkManager.fetchChanges();
        if(changes != null && !changes.isEmpty())
        {
            for(Action action : changes)
            {
                switch(action.actionClass)
                {
                case ACTION_ENEMY_CREATE:

                    ActionEnemyCreate enemyCreate = (ActionEnemyCreate)action;

                    if(enemyManager == null || enemyManager.enemyList == null)
                    {
                        break;
                    }

                    if(!enemyCreate.needsID)
                    {
                        enemyManager.setMultiplierA(enemyCreate.armor);
                        enemyManager.setMultiplierS(enemyCreate.velocity);
                        enemyManager.setMultiplierSP(waveCalculator.getMultiplierSpawnRate(gold));
                        enemyManager.addEnemy(enemyCreate.enemyType, enemyCreate.entityID);

                    }

                    for(Enemy enemy : enemyManager.enemyList)
                    {
                        if(enemy.tempID == enemyCreate.tempID)
                        {
                            enemy.entityID = enemyCreate.entityID;
                        }
                    }
                    break;
                case ACTION_HEALTH_CHANGED:
                    ActionHealthChanged healthChanged = (ActionHealthChanged)action;

                    health = healthChanged.newHealth;
                    break;
                case ACTION_HOST_PAUSE:

                    break;
                case ACTION_TOWER_PLACED:
                    ActionTowerPlaced towerPlaced = (ActionTowerPlaced)action;

                    for(Tower tower : towerManager.towerList)
                    {
                        if(tower.tempID == towerPlaced.tempID)
                        {
                            tower.entityID = towerPlaced.entityID;
                        }
                    }
                    break;
                case ACTION_TRANSFER_RESOURCES:


                    break;
                case ACTION_CREATE_WAVE:
                    ActionCreateWave waveData = (ActionCreateWave)action;

                    enemyManager.setWave(waveData.amountNormalEnemies,
                                         waveData.amountFastEnemies,
                                         waveData.amountHeavyEnemies,
                                         waveData.armorMultiplier,
                                         waveData.speedMultiplier,
                                         waveData.delay);

                    if(enemyManager != null)
                    {
                        enemyManager.setPaused(false);
                    }
                    break;

                case ACTION_WAVE_END:
                    readyButton.setVisible(true);
                    readyButton.setDisabled(false);

                    enemyManager.setPaused(true);
                    break;
                }


            }
        }
    }


    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {}
}
