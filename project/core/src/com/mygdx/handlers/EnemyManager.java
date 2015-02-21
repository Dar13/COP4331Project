package com.mygdx.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.entities.Enemy;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 2/18/2015.
 */
public class EnemyManager {

    public int numEnemies;
    public Texture img;
    public LinkedList<Enemy> enemies;


    public EnemyManager(int numEnemies){
        enemies = new LinkedList<Enemy>();
        img = new Texture("EnemyDev.png");
        this.numEnemies = numEnemies;
    }

    public void AddEnemy(Texture img, float velocity, float armor, LinkedList<WayPoint> path){
        Enemy New = new Enemy(img, velocity, armor, path);
        enemies.addLast(New);
        numEnemies++;
    }

    public void RemoveEnemy(){

    }

    public void UpdateAll(float deltaTime){
        for (int i = 0; i < numEnemies; i++){
            if(!enemies.get(i).Check()){
                enemies.get(i).Move();
            }
        }
    }

    public void RenderAll(SpriteBatch sb){
        for (int i = 0; i < numEnemies; i++){
            enemies.get(i).render(sb);
        }
    }

}

