package com.mygdx.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.entities.Enemy;

import java.util.LinkedList;

/**
 * Created by James on 2/18/2015.
 */
public class EnemyManager {

    public int numEnemies;
    public Texture img;
    public LinkedList<Enemy> enemies;

    public EnemyManager(int numEnemies,float x,float y, float vel,String dir){
        enemies = new LinkedList<Enemy>();
        img = new Texture("EnemyDev.png");
        this.numEnemies = numEnemies;
    }

}

