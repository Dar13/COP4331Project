package com.NewHandlers;

import com.NewEntities.Enemy;
import com.NewEntities.EnemyFactory;
import com.NewEntities.Entity;
import com.NewEntities.NewEnemy;
import com.NewEntities.NewTower;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.MyGame;
import com.mygdx.triggers.WayPoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by James on 3/20/2015.
 */
public class NewEnemyManager extends Actor{
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
    public LinkedList<NewEnemy> enemies;

    public List<Enemy> enemyList;

    public LinkedList<NewTower> towers;
    private LinkedList<WayPoint> path;
    private float multiplierS = 0;
    private float multiplierA = 0;
    private float incrementer = 1;
    private float multiplierSp = 0;

    public NewEnemyManager(LinkedList<WayPoint> path)
    {
        enemies = new LinkedList<NewEnemy>();
        towers = new LinkedList<NewTower>();
        timeSinceLastNorm = 0;
        timeSinceLastFast = 0;
        timeSinceLastHeavy = 0;
        waveToBeSpawnedNorm = waveInfoNorm;
        totalWavesToBeSpawned = waveToBeSpawnedNorm;
        waveToBeSpawnedFast = waveInfoFast;
        waveToBeSpawnedHeavy = waveInfoHeavy;
        this.path = path;

        enemyList = new ArrayList<>();

    }

    /**
     * Creates and adds an enemy to the list of current enemies.
     *
     * @param type
     * @param baseTexture
     * @param otherTexture
     * @param path
     */
    public void addEnemy(Entity.Type type, Texture baseTexture, Texture otherTexture, List<WayPoint> path)
    {
        System.out.println("hi");
        Enemy enemy = EnemyFactory.createEnemy(type, baseTexture, otherTexture, 0, 0);
        enemy.setWayPoints(path);
        enemyList.add(enemy); // this is an append operation, same as addLast()
        numEnemies++;
    }

    //Adds new Normal enemy to the Enemy linked list.
    public void AddEnemy(Texture img, Texture img2, float velocity, float armor, LinkedList<WayPoint> path)
    {
        NewEnemy New = new NewEnemy(img, img2, (velocity * (incrementer + multiplierS)), (armor * (incrementer + multiplierA)), path, NewEnemy.Type.ENEMY_NORMAL);
        enemies.addLast(New);
        numEnemies++;
    }

    //Adds new Fast enemy to the Enemy linked list.
    public void AddFastEnemy(Texture img3, Texture img4, float velocity, float armor, LinkedList<WayPoint> path)
    {
        NewEnemy nEw = new NewEnemy(img3, img4, (velocity * (incrementer + multiplierS)), (armor * (incrementer + multiplierA)), path, NewEnemy.Type.ENEMY_FAST);
        enemies.addLast(nEw);
        numEnemies++;
    }

    //Adds new Heavy enemy to the Enemy linked list.
    public void AddHeavyEnemy(Texture img5, Texture img6, float velocity, float armor, LinkedList<WayPoint> path)
    {
        NewEnemy neW = new NewEnemy(img5, img6, (velocity * (incrementer + multiplierS)), (armor * (incrementer + multiplierA)), path, NewEnemy.Type.ENEMY_HEAVY);
        enemies.addLast(neW);
        numEnemies++;
    }

    //Removes targeted enemy from Enemy Linked list
    public void removeEnemy(int id)
    {
        ListIterator<Enemy> iterator = enemyList.listIterator();
        while(iterator.hasNext())
        {
            if(iterator.next().entityID == id)
            {
                iterator.remove();
            }
        }
        /*
        if (toBeDeleted == 0)
        {
            enemies.removeFirst();
        }
        else
        {
            enemies.remove(toBeDeleted);
        }
        */
    }

    //Calculates the number of enemies to spawn in the next wave based on the multiplier.
    public void NextWave(float multiplier)
    {
        if(currentWave == 2)
        {
            this.waveToBeSpawnedNorm = waveInfoNorm * (1 + (int)multiplier);
            this.waveToBeSpawnedFast = waveInfoFast;
            this.totalWavesToBeSpawned = waveToBeSpawnedNorm + waveToBeSpawnedFast;
        }

        else if(currentWave == 3)
        {
            this.waveToBeSpawnedNorm = waveInfoNorm * (1 + (int)multiplier);
            this.waveToBeSpawnedFast = waveInfoFast * (1 + (int)(multiplier / 2));
            this.waveToBeSpawnedHeavy = waveInfoHeavy;
            this.totalWavesToBeSpawned = waveToBeSpawnedNorm + waveToBeSpawnedFast + waveToBeSpawnedHeavy;
        }

        else
        {
            this.waveToBeSpawnedNorm = waveInfoNorm * (1 + (int)multiplier);
            this.waveToBeSpawnedFast = waveInfoFast * (1 + (int)(multiplier / 2));
            this.waveToBeSpawnedHeavy = waveInfoHeavy * (1 + (int)(multiplier / 3));
            this.totalWavesToBeSpawned = waveToBeSpawnedNorm + waveToBeSpawnedFast + waveToBeSpawnedHeavy;
        }

    }

    //Calculates the multiplier to be used in the next round based on player towers.
    public float NextWaveCalculator()
    {
        float multiplier = 0;
        multiplierS = currentWave * .05f;
        multiplierA = currentWave * .05f;
        for (int i = 0; i < towers.size(); i ++)
        {
            switch (towers.get(i).type)
            {
                case TOWER_RIFLE:
                    multiplier = multiplier + .25f;
                    multiplierS = multiplierS + .01f;
                    multiplierA = multiplierA + .05f;
                    multiplierSp = multiplierSp + .1f;
                    break;
                case TOWER_BAZOOKA:
                    multiplier = multiplier + .5f;
                    multiplierS = multiplierS + .02f;
                    multiplierA = multiplierA + .15f;
                    multiplierSp = multiplierSp + .1f;
                    break;
                case TOWER_SNIPER:
                    multiplier = multiplier + 1;
                    multiplierS = multiplierS + .04f;
                    multiplierA = multiplierA + .17f;
                    multiplierSp = multiplierSp + .1f;

                    break;

            }
        }
        incrementer = incrementer + .07f;
        multiplierSp = multiplierSp + .5f;
        return multiplier;
    }


    /* Spawns new enemies based on wave number, updates the enemy health, checks to see if they are
    dead, removes them if they are, and moves them if they are alive.
     */

    @Override
    public void act(float delta) {
        Update(delta);
    }

    public void SetTowers(LinkedList<NewTower> towers)
    {
        this.towers = towers;
    }

    public void Update(float fps)
    {
        //accumulator +=deltaTime;
        if (totalWavesToBeSpawned == 0 && enemies.size() == 0)
        {
            currentWave++;
            float multiplier = NextWaveCalculator();
            NextWave(multiplier);
        }

        if (currentWave == 1)
        {
            timeSinceLastNorm++;

            if (timeSinceLastNorm > ((MyGame.fpsretrieve/2) - multiplierSp) && waveToBeSpawnedNorm > 0) {
                //AddEnemy(EnemyImg, NullLayer, 3, 1, path);
                addEnemy(Entity.Type.ENEMY_NORMAL, EnemyImg, NullLayer, path);

                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
                totalWavesToBeSpawned--;
            }

        }

        else if (currentWave == 2)
        {
            timeSinceLastNorm++;
            timeSinceLastFast++;

            if (timeSinceLastNorm > ((MyGame.fpsretrieve/2) - multiplierSp) && waveToBeSpawnedNorm > 0)
            {
                //AddEnemy(EnemyImg, NullLayer, 3, 1, path);
                addEnemy(Entity.Type.ENEMY_NORMAL, EnemyImg, NullLayer, path);

                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
                totalWavesToBeSpawned--;
            }

            if (timeSinceLastFast > ((MyGame.fpsretrieve/3) - multiplierSp) && waveToBeSpawnedFast > 0)
            {
                //AddFastEnemy(FastEnemy, NullLayer, 6, 1, path);
                addEnemy(Entity.Type.ENEMY_FAST, EnemyImg, NullLayer, path);

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

            if (timeSinceLastNorm > ((MyGame.fpsretrieve/2) - multiplierSp) && waveToBeSpawnedNorm > 0)
            {
                //AddEnemy(EnemyImg, NullLayer, 3, 1, path);
                addEnemy(Entity.Type.ENEMY_NORMAL, EnemyImg, NullLayer, path);

                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
                totalWavesToBeSpawned--;
            }

            if (timeSinceLastFast > ((MyGame.fpsretrieve/3) - multiplierSp) && waveToBeSpawnedFast > 0)
            {
                //AddFastEnemy(FastEnemy, NullLayer, 6, 1, path);
                addEnemy(Entity.Type.ENEMY_FAST, EnemyImg, NullLayer, path);

                timeSinceLastFast = 0;
                waveToBeSpawnedFast--;
                totalWavesToBeSpawned--;
            }

            if (timeSinceLastHeavy > ((MyGame.fpsretrieve * 3) - multiplierSp) && waveToBeSpawnedHeavy > 0)
            {
                //AddHeavyEnemy(TigerBase, TigerTurret, .5f, 15, path);
                addEnemy(Entity.Type.ENEMY_HEAVY, TigerBase, TigerTurret, path);

                timeSinceLastHeavy = 0;
                waveToBeSpawnedHeavy--;
                totalWavesToBeSpawned--;
            }
        }

        /*
        for(Enemy enemy : enemyList)
        {
            if(enemy != null)
            {
                // TODO: COME BACK WHEN TOWERS ARE REWORKED
            }
        }
        */

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


      /*  for(Enemy enemy : enemyList)
        {
            if(!enemy.isAlive())
            {
                numDeadEnemies++;
                removeEnemy(enemy.entityID);
                numEnemies--;
            }
        }*/


        for (int i = 0; i < enemies.size(); i++)
        {
            if (enemies.get(i).health <= 0)
            {
                numDeadEnemies++;
                removeEnemy(i);
                numEnemies--;
            }
        }

        // Really need to rethink this. Maybe combine previous loops together into one big one?
        for(Enemy enemy : enemyList)
        {
            if(!enemy.check())
            {
                enemy.move();
            }
        }
        /*
        for (int i = 0; i < enemies.size(); i++)
        {
            if (!enemies.get(i).Check())
            {
                enemies.get(i).Move();
            }
        }
        */


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
        /*
        for (int i = 0; i < enemies.size(); i++)
        {
            if (enemies.get(i).atEnd)
            {
                removeEnemy(i);
                numEnemies--;
                enemyAtEnd++;
            }

        }
        */
        for(ListIterator<Enemy> iterator = enemyList.listIterator(); iterator.hasNext();)
        {
            if(iterator.next().isNavigationFinished())
            {
                iterator.remove();
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


    @Override
    public void draw(Batch batch, float parentAlpha) {
        System.out.println( numEnemies + " " + enemies.size());
        for (int i = 0; i < numEnemies; i++)
        {
            System.out.println(i + " " + numEnemies);
            enemyList.get(i).draw(batch,parentAlpha);
        }
    }
}
