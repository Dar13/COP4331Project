package com.mygdx.states;

// LibGDX includes

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.handlers.EnemyManager;
import com.mygdx.handlers.GameStateManager;
import com.mygdx.UI.MyStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.handlers.WayPointManager;
import com.mygdx.handlers.action.Action;
import com.mygdx.net.ConnectionMode;

/**
 * Created by NeilMoore on 2/14/2015.
 */
public class NetTest extends GameState
{
    protected boolean connected = false;
    private MyStage stage;
    private TextButton serverButton;
    private TextButton clientButton;
    private TextButton toMenu;
    private OrthographicCamera cam;
    private Texture Map = new Texture("Maps/SubMenuMap.png");
    private WayPointManager wayPointManager;
    private EnemyManager enemyManager;

    public NetTest(GameStateManager gameStateManager, final NetworkManager networkManager)
    {
        super(gameStateManager, networkManager);

        stage = new MyStage();
        cam = (OrthographicCamera) stage.getCamera();
        cam.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));



        serverButton = new TextButton("Server", skin);
        serverButton.setSize(200, 60);
        serverButton.setPosition(game.V_WIDTH/2-serverButton.getWidth()/2, MyGame.V_HEIGHT * 7/12);
        serverButton.addListener( new ClickListener());

        stage.addActor(serverButton);

        clientButton = new TextButton("Client", skin);
        clientButton.setSize(200, 60);
        clientButton.setPosition(game.V_WIDTH/2-serverButton.getWidth()/2, serverButton.getY() - 65);
        clientButton.addListener( new ClickListener());
        stage.addActor(clientButton);

        toMenu = new TextButton("Menu", skin);
        toMenu.setSize(200, 60);
        toMenu.setPosition(game.V_WIDTH/2-toMenu.getWidth()/2, clientButton.getY() - 65);
        toMenu.addListener(new ClickListener());
        stage.addActor(toMenu);
    }

    @Override
    public void update(float delta)
    {
        stage.act(delta);
        if (serverButton.isPressed()){
            networkManager.prepInitialize(true,
                    ConnectionMode.WIFI_LAN,
                    ConnectionMode.NONE,
                    true);
            serverButton.setDisabled(true);
           // gameStateManager.setState(GameStateManager.LEVELSELECT,0);
          //  gameStateManager.setState(GameStateManager.PLAY,1);
        }

        if(clientButton.isPressed()){
            networkManager.prepInitialize(false,
                    ConnectionMode.WIFI_LAN,
                    ConnectionMode.NONE,
                    true);
            clientButton.setDisabled(true);
           // gameStateManager.setState(GameStateManager.LEVELSELECT,0);
            //gameStateManager.setState(GameStateManager.PLAY,2);
        }

        if(toMenu.isChecked()){
            gameStateManager.setState(GameStateManager.MENU, 0);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        // clear screen, then draw stage
        Gdx.gl.glClearColor(0, 0, 0, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
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
    public void dispose()
    {

    }
}
