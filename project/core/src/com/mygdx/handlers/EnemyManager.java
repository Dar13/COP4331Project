package com.mygdx.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.entities.Enemy;
import com.mygdx.entities.EnemyHeavy;
import com.mygdx.entities.Tower;
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
    protected static final int waveInfoFast = 2;
    protected static final int waveInfoHeavy = 1;

    public int currentWave = 1;
    public int numEnemies = 0;
    public int numHEnemies = 0;
    public int numFEnemies = 0;
    public int waveToBeSpawnedNorm;
    public int waveToBeSpawnedFast;
    public int waveToBeSpawnedHeavy;
    public float timeSinceLastNorm;
    public float timeSinceLastFast;
    public float timeSinceLastHeavy;
    private Texture EnemyImg = new Texture("EnemyDev.png");
    private Texture FastEnemy = new Texture("FastEnemy.png");
    private Texture TigerBase = new Texture("EnemyDev.png");
    private Texture TigerTurret = new Texture("EnemyDev.png");
    public LinkedList<Enemy> enemies;
    public LinkedList<EnemyHeavy> enemyHeavies;
    public LinkedList<Tower> towers;
    private LinkedList<WayPoint> path;

    public float accumulator =0;


    public EnemyManager(LinkedList<WayPoint> path)
    {
        enemies = new LinkedList<Enemy>();
        enemyHeavies = new LinkedList<EnemyHeavy>();
        towers = new LinkedList<Tower>();
        timeSinceLastNorm = 0;
        timeSinceLastFast = 0;
        timeSinceLastHeavy = 0;
        waveToBeSpawnedNorm = waveInfoNorm;
        waveToBeSpawnedFast = 0;
        waveToBeSpawnedHeavy = 0;
        this.path = path;
    }

    public void AddEnemy(Texture img, float velocity, float armor, LinkedList<WayPoint> path)
    {
        Enemy New = new Enemy(img, velocity, armor, path);
        enemies.addLast(New);
        numEnemies++;
    }

    public void AddFastEnemy(Texture img, float velocity, float armor, LinkedList<WayPoint> path)
    {
        Enemy New = new Enemy(img, velocity, armor, path);
        enemies.addLast(New);
        numEnemies++;
    }

    public void AddHeavyEnemy(Texture img, Texture img2, float velocity, float armor, LinkedList<WayPoint> path)
    {
        EnemyHeavy New = new EnemyHeavy(img, img2, velocity, armor, path);
        enemyHeavies.addLast(New);
        numHEnemies++;
    }

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

    public void RemoveHEnemy(int toBeDeleted)
    {
        if (toBeDeleted == 0)
        {
            enemyHeavies.removeFirst();
        }

        else
        {
            enemyHeavies.remove(toBeDeleted);
        }
    }

    public void NextWaveCalculator()
    {

    }

    public void Update(float deltaTime, LinkedList<Tower> towers)
    {
        //accumulator +=deltaTime;


        this.towers = towers;

        if (currentWave == 1) {
            timeSinceLastNorm = timeSinceLastNorm + deltaTime;

            if (timeSinceLastNorm > .3 && waveToBeSpawnedNorm > 0) {
                AddEnemy(EnemyImg, 3, 1, path);
                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
            }
        }

        else if (currentWave == 2) {
            timeSinceLastNorm = timeSinceLastNorm + deltaTime;
            timeSinceLastFast = timeSinceLastFast + deltaTime;

            if (timeSinceLastNorm > .5 && waveToBeSpawnedNorm > 0) {
                AddEnemy(EnemyImg, 3, 1, path);
                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
            }

            if (timeSinceLastFast > .5 && waveToBeSpawnedFast > 0) {
                AddEnemy(FastEnemy, 6, 1, path);
                timeSinceLastFast = 0;
                waveToBeSpawnedFast--;
            }
        }

        else if (currentWave > 2) {
            timeSinceLastNorm = timeSinceLastNorm + deltaTime;
            timeSinceLastFast = timeSinceLastFast + deltaTime;
            timeSinceLastHeavy = timeSinceLastHeavy + deltaTime;

            if (timeSinceLastNorm > .5 && waveToBeSpawnedNorm > 0) {
                AddEnemy(EnemyImg, 3, 1, path);
                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
            }

            if (timeSinceLastFast > .5 && waveToBeSpawnedFast > 0) {
                AddEnemy(FastEnemy, 6, 1, path);
                timeSinceLastFast = 0;
                waveToBeSpawnedFast--;
            }

            if (timeSinceLastHeavy > 3 && waveToBeSpawnedHeavy > 0) {
                AddHeavyEnemy(TigerBase, TigerTurret, .5f, 15, path);
                timeSinceLastFast = 0;
                waveToBeSpawnedFast--;
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

        for (int i = 0; i < enemyHeavies.size(); i++)
        {
            if (enemyHeavies.get(i) == null)
            {
                continue;
            }
            for (int j = 0; j < towers.size(); j++)
            {
                if (InRange(enemyHeavies.get(i).sprite.getX(),
                            enemyHeavies.get(i).sprite.getY(),
                            towers.get(j).sprite.getX(),
                            towers.get(j).sprite.getY(),
                            towers.get(j).range))
                {
                    if (enemyHeavies.get(i).health > 0)
                    {
                        enemyHeavies.get(i).health = enemyHeavies.get(i).health - (towers.get(j).damages / enemyHeavies.get(i).armor);
                    }


                }
            }
        }

        for (int i = 0; i < enemies.size(); i++)
        {
            if (enemies.get(i).health <= 0)
            {
                RemoveEnemy(i);
                numEnemies--;
            }

        }

        for (int i = 0; i < enemyHeavies.size(); i++)
        {
            if (enemyHeavies.get(i).health <= 0)
            {
                RemoveHEnemy(i);
                numHEnemies--;
            }

        }


        for (int i = 0; i < enemies.size(); i++)
        {
            if (!enemies.get(i).Check())
            {
                enemies.get(i).Move();
            }

        }

        for (int i = 0; i < enemyHeavies.size(); i++)
        {
            if (!enemyHeavies.get(i).Check())
            {
                enemyHeavies.get(i).Move();
            }

        }

    }
    public boolean InRange(float enemyX, float enemyY, float towerX, float towerY, float towerRange)
    {
        if (Math.abs(enemyX + centerOffset - towerX + centerOffset)
                < towerRange * rangeOffset
                && Math.abs(enemyY + centerOffset - towerY + centerOffset)
                < towerRange * rangeOffset)
        {
            if (Math.sqrt(Math.pow((enemyX + centerOffset - towerX + centerOffset), 2)
                                  + Math.pow((enemyY + centerOffset - towerY + centerOffset), 2))
                    < towerRange * rangeOffset)
            {
                return true;
            }
        }
        return false;
    }
    public void RenderAll(SpriteBatch sb)
    {
        for (int i = 0; i < numHEnemies; i++)
        {
            enemyHeavies.get(i).render(sb);
        }

        for (int i = 0; i < numEnemies; i++)
        {
            enemies.get(i).render(sb);
        }

    }

}

