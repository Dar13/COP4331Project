package com.mygdx.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Miguel on 3/20/2015.
 */
public class AssetLoader {
    public static Sound startButton;

    public static void load()
    {
        startButton = Gdx.audio.newSound(Gdx.files.internal("data/start.wav"));
    }

    public static void dispose()
    {

    }
}
