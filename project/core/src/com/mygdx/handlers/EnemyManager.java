package com.mygdx.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.entities.Enemy;
import com.mygdx.entities.Tower;
import com.mygdx.game.MyGame;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 2/18/2015.
 */
public class EnemyManager
{
    protected static final int centerOffset = 16;
    protected static final int rangeOffset = 32;
    protected static final int waveInfoNorm = 20;
    protected static final int waveInfoFast = 7;
    protected static final int waveInfoHeavy = 1;

    public int currentWave = 1;
    public int numEnemies = 0;
    private int numDeadEnemies = 0;
    public int waveToBeSpawnedNorm;
    public int waveToBeSpawnedFast;
    public int waveToBeSpawnedHeavy;
    public int totalWavesToBeSpawned;
    public float timeSinceLastNorm;
    public float timeSinceLastFast;
    public float timeSinceLastHeavy;
    private Texture EnemyImg = new Texture("EnemyDev.png");
    private Texture NullLayer = new Texture("nulllayer.png");
    private Texture FastEnemy = new Texture("FastEnemy.png");
    private Texture TigerBase = new Texture("tigerbase.png");
    private Texture TigerTurret = new Texture("tigerturret.png");
    public LinkedList<Enemy> enemies;
    public LinkedList<Tower> towers;
    private LinkedList<WayPoint> path;


    public EnemyManager(LinkedList<WayPoint> path)
    {
        enemies = new LinkedList<Enemy>();
        towers = new LinkedList<Tower>();
        timeSinceLastNorm = 0;
        timeSinceLastFast = 0;
        timeSinceLastHeavy = 0;
        waveToBeSpawnedNorm = waveInfoNorm;
        totalWavesToBeSpawned = waveToBeSpawnedNorm;
        waveToBeSpawnedFast = waveInfoFast;
        waveToBeSpawnedHeavy = waveInfoHeavy;
        this.path = path;

    }

    //Adds new Normal enemy to the Enemy linked list.
    public void AddEnemy(Texture img, Texture img2, float velocity, float armor, LinkedList<WayPoint> path)
    {
        Enemy New = new Enemy(img, img2, velocity, armor, path, Enemy.Type.NORMAL);
        enemies.addLast(New);
        numEnemies++;
    }

    //Adds new Fast enemy to the Enemy linked list.
    public void AddFastEnemy(Texture img3, Texture img4, float velocity, float armor, LinkedList<WayPoint> path)
    {
        Enemy nEw = new Enemy(img3, img4, velocity, armor, path, Enemy.Type.FAST);
        enemies.addLast(nEw);
        numEnemies++;
    }

    //Adds new Heavy enemy to the Enemy linked list.
    public void AddHeavyEnemy(Texture img5, Texture img6, float velocity, float armor, LinkedList<WayPoint> path)
    {
        Enemy neW = new Enemy(img5, img6, velocity, armor, path, Enemy.Type.HEAVY);
        enemies.addLast(neW);
        numEnemies++;
    }

    //Removes targeted enemy from Enemy Linked list
    public void RemoveEnemy(int toBeDeleted)
    {

        if (toBeDeleted == 0)
        {
            enemies.removeFirst();
        }

        else
        {
            enemies.remove(toBeDeleted);
        }


    }

    //Calculates the number of enemies to spawn in the next wave based on the multiplier.
    public void NextWave(float multiplier)
    {
        if(currentWave == 2)
        {
            this.waveToBeSpawnedNorm = waveInfoNorm * (int)multiplier;
            this.waveToBeSpawnedFast = waveInfoFast;
            this.totalWavesToBeSpawned = waveToBeSpawnedNorm + waveToBeSpawnedFast;
        }

        else if(currentWave == 3)
        {
            this.waveToBeSpawnedNorm = waveInfoNorm * (int)multiplier;
            this.waveToBeSpawnedFast = waveInfoFast * (int)(multiplier / 2);
            this.waveToBeSpawnedHeavy = waveInfoHeavy;
            this.totalWavesToBeSpawned = waveToBeSpawnedNorm + waveToBeSpawnedFast + waveToBeSpawnedHeavy;
        }

        else
        {
            this.waveToBeSpawnedNorm = waveInfoNorm * (int)multiplier;
            this.waveToBeSpawnedFast = waveInfoFast * (int)(multiplier / 2);
            this.waveToBeSpawnedHeavy = waveInfoHeavy * (int)(multiplier / 3);
            this.totalWavesToBeSpawned = waveToBeSpawnedNorm + waveToBeSpawnedFast + waveToBeSpawnedHeavy;
        }

    }

    //Calculates the multiplier to be used in the next round based on player towers.
    public float NextWaveCalculator()
    {
        float multiplier = 0;
        for (int i = 0; i < towers.size(); i ++)
        {
            switch (towers.get(i).type)
            {
                case RIFLE:
                    multiplier = multiplier + .25f;
                    break;
                case BAZOOKA:
                    multiplier = multiplier + .5f;
                    break;
                case SNIPER:
                    multiplier = multiplier + 1;
                    break;

            }
        }

        return multiplier;
    }


    /* Spawns new enemies based on wave number, updates the enemy health, checks to see if they are
    dead, removes them if they are, and moves them if they are alive.
     */
    public void Update(float fps, LinkedList<Tower> towers)
    {
        //accumulator +=deltaTime;
        if (totalWavesToBeSpawned == 0 && enemies.size() == 0)
        {
            currentWave++;
            float multiplier = NextWaveCalculator();
            NextWave(multiplier);
        }

        this.towers = towers;

        if (currentWave == 1)
        {
            timeSinceLastNorm++;

            if (timeSinceLastNorm > MyGame.fpsretrieve/2 && waveToBeSpawnedNorm > 0) {
                AddEnemy(EnemyImg, NullLayer, 3, 1, path);
                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
                totalWavesToBeSpawned--;
            }

        }

        else if (currentWave == 2)
        {
            timeSinceLastNorm++;
            timeSinceLastFast++;

            if (timeSinceLastNorm > MyGame.fpsretrieve/2 && waveToBeSpawnedNorm > 0)
            {
                AddEnemy(EnemyImg, NullLayer, 3, 1, path);
                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
                totalWavesToBeSpawned--;
            }

            if (timeSinceLastFast > MyGame.fpsretrieve/3 && waveToBeSpawnedFast > 0)
            {
                AddFastEnemy(FastEnemy, NullLayer, 6, 1, path);
                timeSinceLastFast = 0;
                waveToBeSpawnedFast--;
                totalWavesToBeSpawned--;
            }
        }

        else if (currentWave > 2)
        {
            timeSinceLastNorm++;
            timeSinceLastFast++;
            timeSinceLastHeavy++;

            if (timeSinceLastNorm > MyGame.fpsretrieve/2 && waveToBeSpawnedNorm > 0)
            {
                AddEnemy(EnemyImg, NullLayer, 3, 1, path);
                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
                totalWavesToBeSpawned--;
            }

            if (timeSinceLastFast > MyGame.fpsretrieve/3 && waveToBeSpawnedFast > 0)
            {
                AddFastEnemy(FastEnemy, NullLayer, 6, 1, path);
                timeSinceLastFast = 0;
                waveToBeSpawnedFast--;
                totalWavesToBeSpawned--;
            }

            if (timeSinceLastHeavy > MyGame.fpsretrieve * 3 && waveToBeSpawnedHeavy > 0)
            {
                AddHeavyEnemy(TigerBase, TigerTurret, .5f, 15, path);
                timeSinceLastHeavy = 0;
                waveToBeSpawnedHeavy--;
                totalWavesToBeSpawned--;
            }
        }


        //Enemy health decrementer, very crude atm.
        for (int i = 0; i < enemies.size(); i++)
        {
            if (enemies.get(i) == null)
            {
                continue;
            }
            for (int j = 0; j < towers.size(); j++)
            {
                if (InRange(enemies.get(i).sprite.getX(),
                            enemies.get(i).sprite.getY(),
                            towers.get(j).sprite.getX(),
                            towers.get(j).sprite.getY(),
                            towers.get(j).range))
                {
                    if (enemies.get(i).health > 0)
                    {
                        enemies.get(i).health = enemies.get(i).health - (towers.get(j).damages / enemies.get(i).armor);
                    }
                }
            }
        }


        for (int i = 0; i < enemies.size(); i++)
        {
            if (enemies.get(i).health <= 0)
            {
                numDeadEnemies++;
                RemoveEnemy(i);
                numEnemies--;
            }

        }



        for (int i = 0; i < enemies.size(); i++)
        {
            if (!enemies.get(i).Check())
            {
                enemies.get(i).Move();
            }

        }


    }

    //Returns the number of enemies killed in the update.
    public int GetDeadEnemies()
    {
        int temp = numDeadEnemies;
        numDeadEnemies = 0;
        return temp;
    }

    //Checks to see if there is any enemies at the end waypoint, removes them if there are, then
    //returns the number at the end to the play class.
    public int CheckEnemiesAtEnd()
    {
        int enemyAtEnd = 0;
        for (int i = 0; i < enemies.size(); i++)
        {
            if (enemies.get(i).atEnd)
            {
                RemoveEnemy(i);
                numEnemies--;
                enemyAtEnd++;
            }

        }
        return enemyAtEnd;
    }

    //Calculator to see if the distance from the tower to the enemy is within the tower range.
    public boolean InRange(float enemyX, float enemyY, float towerX, float towerY, float towerRange)
    {
        if (Math.pow((enemyX + centerOffset) - (towerX + centerOffset), 2) + Math.pow(((enemyY + centerOffset) - (towerY + centerOffset)), 2)
            < Math.pow(towerRange * rangeOffset, 2))
        {
            return true;
        }

        return false;
    }

    public void RenderAll(SpriteBatch sb)
    {

        for (int i = 0; i < numEnemies; i++)
        {
            enemies.get(i).render(sb);
        }

    }

}

