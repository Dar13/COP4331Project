package com.mygdx.handlers;

import com.mygdx.entities.Enemy;
import com.mygdx.entities.EnemyFactory;
import com.mygdx.entities.Entity;
import com.mygdx.entities.FastEnemy;
import com.mygdx.entities.HeavyEnemy;
import com.mygdx.entities.NormalEnemy;
import com.mygdx.entities.Tower;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.action.ActionEnemyCreate;
import com.mygdx.handlers.action.ActionEnemyDestroy;
import com.mygdx.handlers.action.ActionEnemyEnd;
import com.mygdx.triggers.WayPoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by James on 3/20/2015.
 */
public class EnemyManager extends Actor{
    public static final int centerOffset = 16;
    public static final int rangeOffset = 32;
    protected static final int waveInfoNorm = 20;
    protected static final int waveInfoFast = 7;
    protected static final int waveInfoHeavy = 1;
    private int tick = 0;

    public int currentWave = 1;
    private int gold = 0;

    protected NetworkManager networkManager;


    public int numEnemies = 0;
    private int numDeadEnemies = 0;
    private int numDeadNormals = 0;
    private int numDeadFast = 0;
    private int numDeadHeavy = 0;
    public int waveToBeSpawnedNorm;
    public int waveToBeSpawnedFast;
    public int waveToBeSpawnedHeavy;
    public int totalWavesToBeSpawned;
    public float timeSinceLastNorm;
    public float timeSinceLastFast;
    public float timeSinceLastHeavy;
    private Texture NormEnemyN = new Texture("Enemydirectionassets/RifleEnemyNorthSheet.png");
    private Texture NormEnemyS = new Texture("Enemydirectionassets/RifleEnemySouthSheet.png");
    private Texture NormEnemyE = new Texture("Enemydirectionassets/RifleEnemyEastSheet.png");
    private Texture NormEnemyW = new Texture("Enemydirectionassets/RifleEnemyWestSheet.png");
    private Texture FastEnemyN = new Texture("Enemydirectionassets/FastEnemyNorthSheet.png");
    private Texture FastEnemyS = new Texture("Enemydirectionassets/FastEnemySouthSheet.png");
    private Texture FastEnemyE = new Texture("Enemydirectionassets/FastEnemyEastSheet.png");
    private Texture FastEnemyW = new Texture("Enemydirectionassets/FastEnemyWestSheet.png");
    private Texture TigerBaseN = new Texture("Enemydirectionassets/TigerNorthSheet.png");
    private Texture TigerBaseS = new Texture("Enemydirectionassets/TigerSouthSheet.png");
    private Texture TigerBaseE = new Texture("Enemydirectionassets/TigerEastSheet.png");
    private Texture TigerBaseW = new Texture("Enemydirectionassets/TigerWestSheet.png");

    public List<Enemy> enemyList;
    public List<Enemy> enemiesToBeRemoved;
    public List<Tower> towerList;
    protected int idCounter = 0;
    protected boolean paused = false;
    protected boolean isSpawn;

    private LinkedList<WayPoint> path;
    private float multiplierS = 1;
    private float multiplierA = 1;
    private float incrementer = 1;
    private float multiplierSp = 1;
    Batch batch;

    public int hashmapCalls = 0;

    public enum NetType
    {
        SERVER,
        CLIENT,
    }

    public EnemyManager(NetworkManager netManager, LinkedList<WayPoint> path, Batch batch)
    {
        networkManager = netManager;
        timeSinceLastNorm = 0;
        timeSinceLastFast = 0;
        timeSinceLastHeavy = 0;
        waveToBeSpawnedNorm = waveInfoNorm;
        totalWavesToBeSpawned = waveToBeSpawnedNorm;
        waveToBeSpawnedFast = waveInfoFast;
        waveToBeSpawnedHeavy = waveInfoHeavy;
        this.path = path;
        this.batch = batch;
        isSpawn = false;

        enemyList = new ArrayList<>();
        enemiesToBeRemoved = new ArrayList<>();
        towerList = new LinkedList<>();
    }

    /**
     * Creates and adds an enemy to the list of current enemies.
     *
     * TempID is should be set by this class.
     * EntityID should be -1 to signify it hasn't been set.
     *
     * @param type
     * @param path
     */
    public void addEnemy(Entity.Type type, Texture north, Texture south, Texture east, Texture west, List<WayPoint> path)
    {
        // entityID = -1 here, tempID = idCounter.
        Enemy enemy = createEnemy(type, -1, idCounter, north, south, east, west);
        idCounter++;

        ActionEnemyCreate actionEnemyCreate = new ActionEnemyCreate(enemy);
        networkManager.addToSendQueue(actionEnemyCreate);

        enemy.setWayPoints(path);
        enemyList.add(enemy); // this is an append operation, same as addLast()
        numEnemies++;
    }

    public void addEnemy(Entity.Type type, int entityID, float health, float armor, float velocity)
    {
        Enemy enemy = null;
        switch(type)
        {
        case ENEMY_NORMAL:
            enemy = createEnemy(type, entityID, -1, NormEnemyN, NormEnemyS, NormEnemyE, NormEnemyW);
            break;
        case ENEMY_FAST:
            enemy = createEnemy(type, entityID, -1, FastEnemyN, FastEnemyS, FastEnemyE, FastEnemyW);
            break;
        case ENEMY_HEAVY:
            enemy = createEnemy(type, entityID, -1, TigerBaseN, TigerBaseS, TigerBaseE, TigerBaseW);
            break;
        default:
            // Failed to add enemy, incorrect type.
            return;
        }

        enemy.setWayPoints(path);
        enemy.setCurrentWayPoint();
        enemy.setHealth(health);
        enemy.setArmor(armor);
        enemy.setVelocity(velocity);
        enemyList.add(enemy);
        numEnemies++;
    }

    protected Enemy createEnemy(Entity.Type type, int entityID, int tempID, Texture north, Texture south, Texture east, Texture west)
    {
        Enemy enemy = EnemyFactory.createEnemy(type, north, south, east, west, 0, 0);
        enemy.applyArmorMultiplier(multiplierA);
        enemy.applyVelocityMultiplier(multiplierS);
        enemy.entityID = entityID;
        enemy.tempID = tempID;

        return enemy;
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
                System.out.println("[EM] Destroying enemy. EntityID = " + enemy.entityID);
                networkManager.addToSendQueue(new ActionEnemyDestroy(enemy));
                enemiesToBeRemoved.add(enemy);
            }
        }
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
        if(!paused) {
            //accumulator +=deltaTime;
            if(tick == 1)
            {

                tick = 0;
            }

            if(isSpawn)
                spawn();

            for (Tower tower : towerList)
            {
                if (tower != null)
                {
                    for (Enemy enemy : enemyList)
                    {
                        if (!(enemy.entityID == tower.getTarget()) && enemy.getDistanceTraveled() > tower.getTargetDistanceTraveled() && tower.inRange(enemy.getPosition(), centerOffset, rangeOffset))
                        {
                            tower.setTarget(enemy.entityID);
                            tower.setTargetDistanceTraveled(enemy.getDistanceTraveled());
                        }
                    }
                }
            }

            for (Tower tower : towerList)
            {
                boolean targetNotdead = false;
                if (tower != null)
                {
                    for (Enemy enemy : enemyList)
                    {
                        if (enemy.entityID == tower.getTarget())
                        {
                            targetNotdead = true;
                            break;
                        }
                    }
                }

                if (!targetNotdead)
                {
                    tower.setTargetDistanceTraveled(0);
                }
            }

            for (Tower tower : towerList)
            {
                if (tower != null)
                {
                    for (Enemy enemy : enemyList)
                    {
                        if (enemy.entityID == tower.getTarget() && !tower.inRange(enemy.getPosition(), centerOffset, rangeOffset))
                        {
                            tower.setTarget(-1);
                            tower.setTargetDistanceTraveled(0);
                        }
                    }
                }
            }


            for (Enemy enemy : enemyList)
            {
                if (enemy != null)
                {
                    for (Tower tower : towerList)
                    {
                        if (tower.inRange(enemy.getPosition(), centerOffset, rangeOffset) && tower.readyToFire() && enemy.entityID == tower.getTarget()) {
                            /**
                             * TODO: Call ActionEnemyDamage
                             * Here be where thar enemies be taking damage me captain.
                             * TODO:Need an action to change the velocity on the server as mortars do that.
                             */
                            enemy.takeDamage(tower.getDamage(enemy.type) / enemy.getArmor());
                            switch (tower.type)
                            {
                                case TOWER_MORTAR:
                                    switch (enemy.type)
                                    {
                                        case ENEMY_NORMAL:
                                            enemy.decrementVelocity(NormalEnemy.BASE_VELOCITY / 2);
                                            break;
                                        case ENEMY_FAST:
                                            enemy.decrementVelocity(FastEnemy.BASE_VELOCITY / 2);
                                            break;
                                        case ENEMY_HEAVY:
                                            enemy.decrementVelocity(HeavyEnemy.BASE_VELOCITY / 2);
                                            break;
                                    }
                            }

                            //System.out.println("Attacking: " + enemy.type + "   Enemy ID: " + enemy.entityID + "   Damage Done: " + (tower.getDamage(enemy.type) / enemy.getArmor()));
                            tower.resetTimeSinceLastShot();

                        }
                    }
                }
            }


            for (Tower tower : towerList)
            {
                tower.updateTimeSinceLastShot();
            }


            // Really need to rethink this. Maybe combine previous loops together into one big one?
            for (Enemy enemy : enemyList)
            {
                if (!enemy.check())
                {
                    enemy.move();
                }
            }

            //System.out.println(numEnemies);

            for (Enemy enemy : enemyList)
            {
                if (!enemy.isAlive())
                {
                    //numDeadEnemies++;
                    if (enemy.type == enemy.type.ENEMY_NORMAL)
                        numDeadNormals++;
                    else if (enemy.type == enemy.type.ENEMY_FAST)
                        numDeadFast++;
                    else if (enemy.type == enemy.type.ENEMY_HEAVY)
                        numDeadHeavy++;
                    removeEnemy(enemy.entityID);
                    numEnemies--;
                }
            }

            enemyList.removeAll(enemiesToBeRemoved);
            enemiesToBeRemoved.clear();

            // netcode

            if (totalWavesToBeSpawned == 0 && enemyList.size() == 0 && isSpawn)
            {
                /**
                 * TODO: would we need to capture a signal from the network manager here instead?
                 * A server would 'command?' that a new wave would start. Would it send all the info
                 * about the wave, or leave that logic on the client?
                 *
                 * Tanner: Yes so far as I know, these three current wave statements would be called
                 * if the client was the spawner client, and then we would need another separate loop
                 * if they were an acceptor client.
                 */
                System.out.println("[EM] Pausing!");
                paused = true;
                tick = 1;
            }
        }

        CheckEnemiesAtEnd();

    }

    protected void spawn()
    {
        if (currentWave > 0 && currentWave < 5)
        {
            timeSinceLastNorm++;

            if (timeSinceLastNorm > ((MyGame.fpsretrieve / 2) - multiplierSp) && waveToBeSpawnedNorm > 0)
            {
                addEnemy(Entity.Type.ENEMY_NORMAL, NormEnemyN, NormEnemyS, NormEnemyE, NormEnemyW, path);

                System.out.println("[EM] Enemy spawned. Normal left = " + waveToBeSpawnedNorm);

                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
                totalWavesToBeSpawned--;
            }

        }
        else if (currentWave > 4 && currentWave < 10)
        {
            timeSinceLastNorm++;
            timeSinceLastFast++;

            if (timeSinceLastNorm > ((MyGame.fpsretrieve / 2) - multiplierSp) && waveToBeSpawnedNorm > 0)
            {
                addEnemy(Entity.Type.ENEMY_NORMAL, NormEnemyN, NormEnemyS, NormEnemyE, NormEnemyW, path);

                System.out.println("[EM] Enemy spawned. Normal left = " + waveToBeSpawnedNorm);
                System.out.println("[EM] Enemy spawned. Fast left = " + waveToBeSpawnedFast);

                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
                totalWavesToBeSpawned--;
            }

            if (timeSinceLastFast > ((MyGame.fpsretrieve / 3) - multiplierSp) && waveToBeSpawnedFast > 0)
            {
                addEnemy(Entity.Type.ENEMY_FAST, FastEnemyN, FastEnemyS, FastEnemyE, FastEnemyW, path);

                System.out.println("[EM] Enemy spawned. Normal left = " + waveToBeSpawnedNorm);
                System.out.println("[EM] Enemy spawned. Fast left = " + waveToBeSpawnedFast);

                timeSinceLastFast = 0;
                waveToBeSpawnedFast--;
                totalWavesToBeSpawned--;
            }
        }
        else if (currentWave > 9)
        {
            timeSinceLastNorm++;
            timeSinceLastFast++;
            timeSinceLastHeavy++;

            if (timeSinceLastNorm > ((MyGame.fpsretrieve / 2) - multiplierSp) && waveToBeSpawnedNorm > 0)
            {
                addEnemy(Entity.Type.ENEMY_NORMAL, NormEnemyN, NormEnemyS, NormEnemyE, NormEnemyW, path);

                timeSinceLastNorm = 0;
                waveToBeSpawnedNorm--;
                totalWavesToBeSpawned--;
            }

            if (timeSinceLastFast > ((MyGame.fpsretrieve / 3) - multiplierSp) && waveToBeSpawnedFast > 0)
            {
                addEnemy(Entity.Type.ENEMY_FAST, FastEnemyN, FastEnemyS, FastEnemyE, FastEnemyW, path);

                timeSinceLastFast = 0;
                waveToBeSpawnedFast--;
                totalWavesToBeSpawned--;
            }

            if (timeSinceLastHeavy > ((MyGame.fpsretrieve * 3) - multiplierSp) && waveToBeSpawnedHeavy > 0)
            {
                addEnemy(Entity.Type.ENEMY_HEAVY, TigerBaseN, TigerBaseS, TigerBaseE, TigerBaseW, path);

                timeSinceLastHeavy = 0;
                waveToBeSpawnedHeavy--;
                totalWavesToBeSpawned--;
            }
        }
    }

    //Returns the amount of gold earned for enemies killed in the update.
    public int GetGoldEarned()
    {
        int temp = numDeadNormals*10 + numDeadFast*5 + numDeadHeavy*15;
        numDeadHeavy = 0;
        numDeadFast = 0;
        numDeadNormals = 0;
        return temp;
    }

    public void setGold(int gold){ this.gold = gold; }

    //Checks to see if there is any enemies at the end waypoint, removes them if there are, then
    //returns the number at the end to the play class.
    // Also sends enemies that are at end info to the server
    public void CheckEnemiesAtEnd()
    {
        for(ListIterator<Enemy> iterator = enemyList.listIterator(); iterator.hasNext();)
        {
            Enemy enemy = iterator.next();
            if(enemy.isNavigationFinished())
            {
                iterator.remove();
                numEnemies--;

                networkManager.addToSendQueue(new ActionEnemyEnd(enemy));
            }
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.batch = batch;

      //  Enemy[] e = enemyList.toArray(<Enemy> e);
        for (int i = 0; i < numEnemies; i++)
        {
         //   System.out.println(enemyList.size() + " " + i);
            if(enemyList.size()>i){
                enemyList.get(i).draw(batch, parentAlpha);
            }


        }
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(boolean toPause)
    {
        paused = toPause;
    }

    public void LevelSelectWave(){
        currentWave = 10;
        waveToBeSpawnedNorm = 999;
        waveToBeSpawnedFast = 999;
        waveToBeSpawnedHeavy = 999;
        totalWavesToBeSpawned = waveToBeSpawnedNorm + waveToBeSpawnedFast + waveToBeSpawnedHeavy;
    }

    public void setWave(int waveToBeSpawnedNorm,
                        int waveToBeSpawnedFast,
                        int waveToBeSpawnedHeavy,
                        float multiplierA,
                        float multiplierS,
                        float multiplierSp )
    {
        this.waveToBeSpawnedNorm = waveToBeSpawnedNorm;
        this.waveToBeSpawnedFast = waveToBeSpawnedFast;
        this.waveToBeSpawnedHeavy = waveToBeSpawnedHeavy;
        this.multiplierA = multiplierA;
        this.multiplierS = multiplierS;
        this.multiplierSp = multiplierSp;
        this.totalWavesToBeSpawned = waveToBeSpawnedNorm + waveToBeSpawnedFast + waveToBeSpawnedHeavy;

        // Otherwise EnemyManager wasn't unpausing due to the timing of when it was
        // receiving the ActionCreateWave packet.
        if(paused && isSpawn)
        {
            paused = false;
        }
    }

    public boolean isSpawn()
    {
        return isSpawn;
    }

    public void setSpawn(boolean isSpawn)
    {
        this.isSpawn = isSpawn;
    }

    public void setMultiplierA(float multiplierA)
    {
        this.multiplierA = multiplierA;
    }

    public void setMultiplierS(float multiplierS)
    {
        this.multiplierS = multiplierS;
    }

    public void setMultiplierSP(float multiplierSp)
    {
        this.multiplierSp = multiplierSp;
    }


}
