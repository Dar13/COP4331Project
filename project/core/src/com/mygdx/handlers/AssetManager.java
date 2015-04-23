package com.mygdx.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

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
        switch(soundID){
            case 1:
                sound = Gdx.audio.newSound(Gdx.files.internal("data/M1.ogg"));
                break;
            case 2:
                sound = Gdx.audio.newSound(Gdx.files.internal("data/Ping.ogg"));
                break;
            case 3:
                sound = Gdx.audio.newSound(Gdx.files.internal("data/Reload.ogg"));
                break;
            default:
                sound = Gdx.audio.newSound(Gdx.files.internal("data/M1.ogg"));
                break;
        }
    }

    public static void loadMusic(int musicID)
    {
        if (music != null) {
            music.dispose();
        }
        switch(musicID){
            case 1:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/MainMenu.ogg"));
                break;
            case 2:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/End.ogg"));
                break;
            case 3:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/Win.ogg"));
                break;
            case 4:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/WinClean.ogg"));
                break;
            default:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/M1.ogg"));
                break;
        }
    }

    public static void dispose()
    {
        if (sound != null) {
            sound.stop();
            sound.dispose();
        }
        if (music != null) {
            music.stop();
            music.dispose();
        }
    }
}
