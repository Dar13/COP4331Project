package com.mygdx.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Miguel on 3/20/2015.
 *
 * Could be used to load maps, sounds, and music. Each would
 * have its own load function in here as well as a 'switch'
 * for the different things we want to load.
 */

public class AssetManager {
    public static Sound sound;
    public static Music music;

    public static void loadSound(int soundID)
    {
        if (sound != null) {
            sound.dispose();
        }
        switch(soundID){
            case 1:
                sound = Gdx.audio.newSound(Gdx.files.internal("data/start.wav"));
                break;
            default:
                sound = Gdx.audio.newSound(Gdx.files.internal("data/epic.ogg"));
                break;
        }
    }

    public static void loadMusic(int musicID)
    {
        switch(musicID){
            case 1:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/epic.wav"));
                break;
            default:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/bass.mp3"));
                break;
        }
    }

    public static void dispose()
    {

    }
}
