package com.NewHandlers;

import com.NewEntities.Enemy;
import com.NewEntities.EnemyFactory;
import com.NewEntities.Entity;
import com.NewEntities.Tower;
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
    public static final int centerOffset = 16;
    public static final int rangeOffset = 32;
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
    private Texture NormEnemy = new Texture("EnemyDev.png");
    private Texture NullLayer = new Texture("nulllayer.png");
    private Texture FastEnemy = new Texture("FastEnemy.png");
    private Texture TigerBase = new Texture("tigerbase.png");
    private Texture TigerTurret = new Texture("tigerturret.png");

    public List<Enemy> enemyList;
    public List<Enemy> enemiesToBeRemoved;
    public List<Tower> towerList;
    protected int idCounter = 0;

    private LinkedList<WayPoint> path;
    private float multiplierS = 0;
    private float multiplierA = 0;
    private float incrementer = 1;
    private float multiplierSp = 0;
    Batch batch;

    public NewEnemyManager(LinkedList<WayPoint> path, Batch batch)
    {
        timeSinceLastNorm = 0;
        timeSinceLastFast = 0;
        timeSinceLastHeavy = 0;
        waveToBeSpawnedNorm = waveInfoNorm;
        totalWavesToBeSpawned = waveToBeSpawnedNorm;
        waveToBeSpawnedFast = waveInfoFast;
        waveToBeSpawnedHeavy = waveInfoHeavy;
        this.path = path;
        this.batch = batch;

        enemyList = new ArrayList<>();
        enemiesToBeRemoved = new ArrayList<>();
        towerList = new LinkedList<>();
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
        Enemy enemy = EnemyFactory.createEnemy(type, baseTexture, otherTexture, 0, 0);
        enemy.entityID = idCounter;
        idCounter++; // TODO: This will have to be changed to reflect getting the true entity ID from the host.



        enemy.setWayPoints(path);
        enemyList.add(enemy); // this is an append operation, same as addLast()
        numEnemies++;


    }

    //Removes targeted enemy from Enemy Linked list
    public void removeEnemy(int id)
    {
        ListIterator<Enemy> iterator = enemyList.listIterator();
        while(iterator.hasNext())
        {
            Enemy enemy = iterator.next();
            if(enemy.entityID == id)
            {
                enemiesToBeRemoved.add(enemy);
            }
        }
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
        for(Tower tower : towerList)
        {
            switch(tower.type)
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

    public void SetTowers(List<Tower> towers)
    {
        towerList = towers;
    }

    public void Update(float fps)
    {
        //accumulator +=deltaTime;
        if (totalWavesToBeSpawned == 0 && enemyList.size() == 0)
        {
            currentWave++;
            float multiplier = NextWaveCalculator();
            NextWave(multiplier);
        }

        if (currentWave == 1)
        {
            timeSinceLastNorm++;

            if (timeSinceLastNorm > ((MyGame.fpsretrieve/2) - multiplierSp) && waveToBeSpawnedNorm > 0) {
                //AddEnemy(NormEnemy, NullLayer, 3, 1, path);
                addEnemy(Entity.Type.ENEMY_NORMAL, NormEnemy, NullLayer, path);

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
                //AddEnemy(NormEnemy, NullLayer, 3, 1, path);
                addEnemy(Entity.Type.ENEMY_NORMAL, NormEnemy, NullLayer, path);

                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
                totalWavesToBeSpawned--;
            }

            if (timeSinceLastFast > ((MyGame.fpsretrieve/3) - multiplierSp) && waveToBeSpawnedFast > 0)
            {
                //AddFastEnemy(FastEnemy, NullLayer, 6, 1, path);
                addEnemy(Entity.Type.ENEMY_FAST, FastEnemy, NullLayer, path);

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
                //AddEnemy(NormEnemy, NullLayer, 3, 1, path);
                addEnemy(Entity.Type.ENEMY_NORMAL, NormEnemy, NullLayer, path);

                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
                totalWavesToBeSpawned--;
            }

            if (timeSinceLastFast > ((MyGame.fpsretrieve/3) - multiplierSp) && waveToBeSpawnedFast > 0)
            {
                //AddFastEnemy(FastEnemy, NullLayer, 6, 1, path);
                addEnemy(Entity.Type.ENEMY_FAST, FastEnemy, NullLayer, path);

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

        for(Tower tower: towerList)
        {
            if(tower != null)
            {
                for(Enemy enemy : enemyList){
                    if(!(enemy.entityID == tower.getTarget()) && enemy.getDistanceTraveled() > tower.getTargetDistanceTraveled() && tower.inRange(enemy.getPosition(), centerOffset, rangeOffset)){
                        tower.setTarget(enemy.entityID);
                        tower.setTargetDistanceTraveled(enemy.getDistanceTraveled());
                    }
                }
            }
        }

        for(Tower tower: towerList)
        {
            boolean targetNotdead = false;
            if(tower != null)
            {
                for(Enemy enemy : enemyList){
                    if(enemy.entityID == tower.getTarget()){
                        targetNotdead = true;
                        break;
                    }
                }
            }

            if (!targetNotdead) {
                tower.setTargetDistanceTraveled(0);
            }
        }

        for(Tower tower: towerList)
        {
            if(tower != null)
            {
                for(Enemy enemy : enemyList){
                    if(enemy.entityID == tower.getTarget() && !tower.inRange(enemy.getPosition(), centerOffset, rangeOffset)){
                        tower.setTarget(-1);
                        tower.setTargetDistanceTraveled(0);
                    }
                }
            }
        }


        for(Enemy enemy : enemyList)
        {
            if(enemy != null)
            {
                for(Tower tower : towerList)
                {
                    if(tower.inRange(enemy.getPosition(), centerOffset, rangeOffset) && tower.readyToFire() && enemy.entityID == tower.getTarget())
                    {
                        enemy.takeDamage(tower.getDamage(enemy.type) / enemy.getArmor());
                        switch (tower.type){
                            case TOWER_MORTAR:
                                enemy.decrimentVelocity(2);
                        }

                        //System.out.println("Attacking: " + enemy.type + "   Enemy ID: " + enemy.entityID + "   Damage Done: " + (tower.getDamage(enemy.type) / enemy.getArmor()));
                        tower.resetTimeSinceLastShot();

                    }
                }
            }
        }


        for(Tower tower : towerList)
        {
            tower.updateTimeSinceLastShot();
        }

        //Enemy health decrementer, very crude atm.
        /*
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
        */




        /*
        for (int i = 0; i < enemies.size(); i++)
        {
            if (enemies.get(i).health <= 0)
            {
                numDeadEnemies++;
                removeEnemy(i);
                numEnemies--;
            }
        }
        */

        // Really need to rethink this. Maybe combine previous loops together into one big one?
        for(Enemy enemy : enemyList)
        {
            if(!enemy.check())
            {
                enemy.move();
            }
        }

        for(Enemy enemy : enemyList)
        {
            if(!enemy.isAlive())
            {
                numDeadEnemies++;
                removeEnemy(enemy.entityID);
                numEnemies--;
            }
        }

        enemyList.removeAll(enemiesToBeRemoved);
        enemiesToBeRemoved.clear();
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


    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.batch = batch;
        for (int i = 0; i < numEnemies; i++)
        {
            enemyList.get(i).draw(batch,parentAlpha);
        }
    }
}
