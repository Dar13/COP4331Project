package com.NewStates;


import com.NewEntities.Actor;
import com.NewEntities.BazookaTower;
import com.NewEntities.Entity;
import com.NewEntities.RifleTower;
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

    private boolean clicked = false;

    //private LinkedList<NewTower> towers;
    private List<Tower> towers;
    public NewEnemyManager enemyManager;
    public NewTowerManager towerManager;
    public WayPointManager wayPointManager;

    private TextButton rifleButton;
    private TextButton bazookaButton;

    private MyStage stage;
    private Texture mapImg;
    private Debugger debugger;

    private OrthographicCamera camera;

    Sprite towerToBePlaced;
    Sprite towerToBePlacedS;

    Texture rifleTowerTexture;
    Texture bazookaTowerTexture;
    Texture towerShadowTexture;

    private BitmapFont font;
    private Batch batch;


    public NewPlay(NewGameStateManager gameStateManager,NetworkManager networkManager, boolean inAndroid)
    {
        super(gameStateManager,networkManager);
        stage = new MyStage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));
        mapImg = new Texture("MapEasy.png");
        Actor map = new Actor(mapImg,0,0);

        // load textures
        rifleTowerTexture = new Texture(NewTowerManager.rifleTexturePath);
        bazookaTowerTexture = new Texture(NewTowerManager.bazookaTexturePath);
        towerShadowTexture = new Texture(NewTowerManager.shadowTexturePath);

        towers = new LinkedList<Tower>();
        wayPointManager = new WayPointManager(inAndroid);
        enemyManager = new NewEnemyManager(wayPointManager.wayPoints);
        towerManager = new NewTowerManager(towers);
        rifleButton = new TextButton("rifle",skin);
        rifleButton.setSize(64,64);
        rifleButton.setPosition(game.V_WIDTH - rifleButton.getWidth(),
                                game.V_HEIGHT - rifleButton.getHeight());
        rifleButton.addListener(new ClickListener());
        bazookaButton = new TextButton("bazooka",skin);
        bazookaButton.setSize(64,64);
        bazookaButton.setPosition(rifleButton.getX(),rifleButton.getY() - 64);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.scale(.01f);


        stage.addActor(map);
        stage.addActor(enemyManager);
        stage.addActor(towerManager);
        stage.addActor(rifleButton);
        stage.addActor(bazookaButton);

    }

    @Override
    public void update() {
        //((OrthographicCamera)stage.getCamera()).zoom += .01;
        boolean clearedForPlacement = true;
        health = health - enemyManager.CheckEnemiesAtEnd();
        gold = gold + (enemyManager.GetDeadEnemies() * 15);

        // rifle button handling
        if(rifleButton.isPressed() && towerPlacement == 0 &&
           gold >= RifleTower.PRICE){
            System.out.println("test - rifle button");
            towerToBePlaced = new Sprite(rifleTowerTexture);
            towerToBePlacedS = new Sprite(towerShadowTexture);
            towerToBePlaced.setPosition(stage.screenToStageCoordinates(new Vector2(MyInput.x,MyInput.y)).x,
                                        stage.screenToStageCoordinates(new Vector2(MyInput.x,MyInput.y)).y);
            towerToBePlacedS.setPosition(Gdx.input.getX()+ 9,MyGame.V_HEIGHT - Gdx.input.getY() - 23);
            towerToBePlacedS.rotate(-45);
            towerPlacement = 1;
            Rifle = 1;
        }

        // bazooka button handling
        if(bazookaButton.isPressed() && towerPlacement==0 &&
           gold >= BazookaTower.PRICE){
            System.out.println("test - bazooka button");
            towerToBePlaced = new Sprite(bazookaTowerTexture);
            towerToBePlacedS = new Sprite(towerShadowTexture);
            towerToBePlaced.setPosition(stage.screenToStageCoordinates(new Vector2(MyInput.x,MyInput.y)).x,
                                        stage.screenToStageCoordinates(new Vector2(MyInput.x,MyInput.y)).y);
            towerToBePlacedS.setPosition(Gdx.input.getX()+ 9,MyGame.V_HEIGHT - Gdx.input.getY() - 23);
            towerToBePlacedS.rotate(-45);
            towerPlacement = 1;
            Zooka = 1;
        }

        // sniper button handling
        // TODO SNIPER BUTTON

        if(towerPlacement == 1) {
            if (wayPointManager.WithinAny(towerToBePlaced.getX(), towerToBePlaced.getY())) {
                clearedForPlacement = false;
            } else if (towerToBePlaced.getX() > 640 || towerToBePlaced.getY() > 480 || towerToBePlaced.getX() + 32 > 640 || towerToBePlaced.getY() + 32 > 480) {
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
                towerManager.addTower(Tower.Type.TOWER_RIFLE, stageCoordinates.x, stageCoordinates.y);
                towerPlacement--;
                Rifle--;
                gold = gold - RifleTower.PRICE;
            }

            else if(Zooka == 1)
            {
                //towerManager.addBazookaTower(stage.screenToStageCoordinates(new Vector2(MyInput.x,MyInput.y)).x,
                //                             stage.screenToStageCoordinates(new Vector2(MyInput.x,MyInput.y)).y);
                towerManager.addTower(Tower.Type.TOWER_BAZOOKA, stageCoordinates.x, stageCoordinates.y);
                towerPlacement--;
                Zooka--;
                gold = gold - BazookaTower.PRICE;
            }
        }

        if (towerPlacement == 1)
        {
            towerToBePlaced.setPosition(stage.screenToStageCoordinates(new Vector2(MyInput.x,MyInput.y)).x,
                                        stage.screenToStageCoordinates(new Vector2(MyInput.x,MyInput.y)).y);
            towerToBePlacedS.setPosition(Gdx.input.getX()+ 9,MyGame.V_HEIGHT - Gdx.input.getY() - 23);
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
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        enemyManager.SetTowers(towers);
        stage.act(delta);
        stage.draw();
        batch = stage.getBatch();
        batch.begin();
        if(towerPlacement == 1)
        {
            towerToBePlacedS.draw(batch);
            towerToBePlaced.draw(batch);
        }
        font.draw(batch, "Health: " + health, 0, MyGame.V_HEIGHT - 10);
        font.draw(batch, "Gold: " + gold, 96, MyGame.V_HEIGHT - 10);
        font.draw(batch, "Wave: " + enemyManager.currentWave, 192, MyGame.V_HEIGHT - 10);
        batch.end();
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
