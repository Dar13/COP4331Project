package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGame;
import com.mygdx.game.desktop.net.PCLocalNetwork;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.net.NetworkInterface;

import java.util.HashMap;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = MyGame.V_WIDTH;
        config.height = MyGame.V_HEIGHT;
        HashMap<NetworkManager.ConnectionMode,
                NetworkInterface> networkImpls = new HashMap<NetworkManager.ConnectionMode,
                                                             NetworkInterface>();

        networkImpls.put(NetworkManager.ConnectionMode.WIFI_LAN, new PCLocalNetwork());
        networkImpls.put(NetworkManager.ConnectionMode.WIFI_P2P, null);
        networkImpls.put(NetworkManager.ConnectionMode.DATA_4G, null);
        networkImpls.put(NetworkManager.ConnectionMode.NONE, null);
		new LwjglApplication(new MyGame(networkImpls), config);
	}
}
