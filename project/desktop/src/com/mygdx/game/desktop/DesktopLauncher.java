package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGame;
import com.mygdx.game.desktop.net.PCLocalNetwork;
import com.mygdx.net.ConnectionMode;
import com.mygdx.net.NetworkInterface;

import java.util.HashMap;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = " Over The Rhine";
        config.width = MyGame.V_WIDTH;
        config.height = MyGame.V_HEIGHT;
        config.resizable = false;
        HashMap<ConnectionMode,
                NetworkInterface> networkImpls = new HashMap<ConnectionMode,
                                                             NetworkInterface>();

        networkImpls.put(ConnectionMode.WIFI_LAN, new PCLocalNetwork());
        networkImpls.put(ConnectionMode.WIFI_P2P, null);
        networkImpls.put(ConnectionMode.DATA_4G, null);
        networkImpls.put(ConnectionMode.NONE, null);

		new LwjglApplication(new MyGame(networkImpls, false), config);
	}
}
