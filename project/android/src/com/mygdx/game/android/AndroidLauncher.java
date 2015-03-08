package com.mygdx.game.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGame;
import com.mygdx.game.android.net.AndroidLocalNetwork;
import com.mygdx.handlers.NetworkManager;
import com.mygdx.net.NetworkInterface;

import java.util.HashMap;

public class AndroidLauncher extends AndroidApplication {
    public boolean inAndroid = false;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        inAndroid = true;
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        HashMap<NetworkManager.ConnectionMode, NetworkInterface> networkImpls = new HashMap<>();

        networkImpls.put(NetworkManager.ConnectionMode.WIFI_LAN, new AndroidLocalNetwork(getContext()));
        networkImpls.put(NetworkManager.ConnectionMode.WIFI_P2P, null);
        networkImpls.put(NetworkManager.ConnectionMode.DATA_4G, null);
        networkImpls.put(NetworkManager.ConnectionMode.NONE, null);

		initialize(new MyGame(networkImpls, inAndroid), config);
	}
}
