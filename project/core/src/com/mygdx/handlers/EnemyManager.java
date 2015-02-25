package com.mygdx.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.entities.Enemy;
import com.mygdx.entities.Tower;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 2/18/2015.
 */
public class EnemyManager {

    public int numEnemies;
    public Texture img;
    public LinkedList<Enemy> enemies;
    public LinkedList<Tower> towers;


    public EnemyManager(int numEnemies){
        enemies = new LinkedList<Enemy>();
        towers = new LinkedList<Tower>();
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

    public void UpdateAll(float deltaTime, LinkedList<Tower> towers)
    {
        this.towers = towers;

        //Enemy health decrimenter, very crude atm.
        for (int i = 0; i < numEnemies; i++){
            for (int j = 0; j < towers.size(); j++){
                if (Math.abs(enemies.get(i).sprite.getX() + 16 - towers.get(j).sprite.getX() + 16)
                    < towers.get(j).range * 32
                    && Math.abs(enemies.get(i).sprite.getY() + 16 - towers.get(j).sprite.getY() + 16)
                    < towers.get(j).range * 32)
                {
                    if(Math.sqrt(Math.pow((enemies.get(i).sprite.getX() + 16 - towers.get(j).sprite.getX() + 16), 2)
                        + Math.pow((enemies.get(i).sprite.getY() + 16 - towers.get(j).sprite.getY() + 16), 2))
                        < towers.get(j).range * 32)
                    {
                        if(enemies.get(i).health > 0){
                            enemies.get(i).health = enemies.get(i).health - towers.get(j).damages / enemies.get(i).armor;
                        }

                    }
                }
            }
        }


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

