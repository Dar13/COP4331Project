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
    private Texture FastEnemyS = new Texture("Enemydirectionassets/FastEnemySouthSheet.png");
    private Texture NormEnemyN = new Texture("Enemydirectionassets/EnemyRifleNorth.png");
    private Texture NormEnemyS = new Texture("Enemydirectionassets/EnemyRifleSouth.png");
    private Texture NormEnemyE = new Texture("Enemydirectionassets/EnemyRifleEast.png");
    private Texture NormEnemyW = new Texture("Enemydirectionassets/EnemyRifleWest.png");
    private Texture FastEnemyN = new Texture("Enemydirectionassets/EnemyFastNorth.png");
    private Texture FastEnemyE = new Texture("Enemydirectionassets/EnemyFastEast.png");
    private Texture FastEnemyW = new Texture("Enemydirectionassets/EnemyFastWest.png");
    private Texture TigerBaseN = new Texture("Enemydirectionassets/TigerNorth.png");
    private Texture TigerBaseS = new Texture("Enemydirectionassets/TigerSouth.png");
    private Texture TigerBaseE = new Texture("Enemydirectionassets/TigerEast.png");
    private Texture TigerBaseW = new Texture("Enemydirectionassets/TigerWest.png");

    public List<Enemy> enemyList;
    public List<Enemy> enemiesToBeRemoved;
    public List<Tower> towerList;
    protected int idCounter = 0;
    protected boolean paused = false;

    private LinkedList<WayPoint> path;
    private float multiplierS = 0;
    private float multiplierA = 0;
    private float incrementer = 1;
    private float multiplierSp = 0;
    Batch batch;

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

        enemyList = new ArrayList<>();
        enemiesToBeRemoved = new ArrayList<>();
        towerList = new LinkedList<>();
    }

    /**
     * Creates and adds an enemy to the list of current enemies.
     *
     * @param type
     * @param path
     */
    public void addEnemy(Entity.Type type, Texture north, Texture south, Texture east, Texture west, List<WayPoint> path)
    {
        Enemy enemy = EnemyFactory.createEnemy(type, north, south, east, west, 0, 0);
        enemy.tempID = idCounter;
        idCounter++;

        System.out.format("Entity created tempID=%d\n", idCounter);

        ActionEnemyCreate actionEnemyCreate = new ActionEnemyCreate(enemy);
        networkManager.addToSendQueue(actionEnemyCreate);

        System.out.format("ActionEnemyCreate sent to networkmanager queue.\n");

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
                networkManager.addToSendQueue(new ActionEnemyDestroy(enemy));
                enemiesToBeRemoved.add(enemy);
            }
        }
    }

    //Calculates the number of enemies to spawn in the next wave based on the multiplier.
    public void NextWave(float multiplier)
    {
        if(currentWave < 5)
        {
            this.waveToBeSpawnedNorm = waveInfoNorm * (1 + (int) multiplier);
            this.totalWavesToBeSpawned = waveToBeSpawnedNorm;
            System.out.println(totalWavesToBeSpawned);
        }

        else if(currentWave > 5 && currentWave < 10)
        {
            this.waveToBeSpawnedNorm = waveInfoNorm * (1 + (int) multiplier);
            this.waveToBeSpawnedFast = waveInfoFast;
            this.totalWavesToBeSpawned = waveToBeSpawnedNorm + waveToBeSpawnedFast;
        }

        else if(currentWave == 10)
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
        float multiplier = 0 + (gold * .001f);
        multiplierS = (currentWave * .05f) + (gold * .001f);
        multiplierA = currentWave * .05f + (gold * .001f);
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
            case TOWER_MORTAR:
                multiplier = multiplier + 1.3f;
                multiplierS = multiplierS + .04f;
                multiplierA = multiplierA + .17f;
                multiplierSp = multiplierSp + .1f;
                break;
            }
        }

        incrementer = incrementer + .07f;
        multiplierSp = multiplierSp + .5f + (gold * .001f);
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
        if(!paused) {
            //accumulator +=deltaTime;


            if (currentWave > 0 && currentWave < 5)
            {
                timeSinceLastNorm++;

                if (timeSinceLastNorm > ((MyGame.fpsretrieve / 2) - multiplierSp) && waveToBeSpawnedNorm > 0)
                {
                    //AddEnemy(NormEnemy, NullLayer, 3, 1, path);
                    addEnemy(Entity.Type.ENEMY_NORMAL, NormEnemyN, NormEnemyS, NormEnemyE, NormEnemyW, path);

                    timeSinceLastNorm = 0;
                    waveToBeSpawnedNorm--;
                    totalWavesToBeSpawned--;
                    System.out.println(totalWavesToBeSpawned);
                }

            }
            else if (currentWave > 4 && currentWave < 10)
            {
                timeSinceLastNorm++;
                timeSinceLastFast++;

                if (timeSinceLastNorm > ((MyGame.fpsretrieve / 2) - multiplierSp) && waveToBeSpawnedNorm > 0)
                {
                    //AddEnemy(NormEnemy, NullLayer, 3, 1, path);
                    addEnemy(Entity.Type.ENEMY_NORMAL, NormEnemyN, NormEnemyS, NormEnemyE, NormEnemyW, path);

                    timeSinceLastNorm = 0;
                    waveToBeSpawnedNorm--;
                    totalWavesToBeSpawned--;
                }

                if (timeSinceLastFast > ((MyGame.fpsretrieve / 3) - multiplierSp) && waveToBeSpawnedFast > 0)
                {
                    //AddFastEnemy(FastEnemy, NullLayer, 6, 1, path);
                    addEnemy(Entity.Type.ENEMY_FAST, FastEnemyN, FastEnemyS, FastEnemyE, FastEnemyW, path);

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
                    //AddEnemy(NormEnemy, NullLayer, 3, 1, path);
                    addEnemy(Entity.Type.ENEMY_NORMAL, NormEnemyN, NormEnemyS, NormEnemyE, NormEnemyW, path);

                    timeSinceLastNorm = 0;
                    waveToBeSpawnedNorm--;
                    totalWavesToBeSpawned--;
                }

                if (timeSinceLastFast > ((MyGame.fpsretrieve / 3) - multiplierSp) && waveToBeSpawnedFast > 0)
                {
                    //AddFastEnemy(FastEnemy, NullLayer, 6, 1, path);
                    addEnemy(Entity.Type.ENEMY_FAST, FastEnemyN, FastEnemyS, FastEnemyE, FastEnemyW, path);

                    timeSinceLastFast = 0;
                    waveToBeSpawnedFast--;
                    totalWavesToBeSpawned--;
                }

                if (timeSinceLastHeavy > ((MyGame.fpsretrieve * 3) - multiplierSp) && waveToBeSpawnedHeavy > 0)
                {
                    //AddHeavyEnemy(TigerBase, TigerTurret, .5f, 15, path);
                    addEnemy(Entity.Type.ENEMY_HEAVY, TigerBaseN, TigerBaseS, TigerBaseE, TigerBaseW, path);

                    timeSinceLastHeavy = 0;
                    waveToBeSpawnedHeavy--;
                    totalWavesToBeSpawned--;
                }
            }

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
            CheckEnemiesAtEnd();


            if (totalWavesToBeSpawned == 0 && enemyList.size() == 0)
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
                currentWave++;
                paused = true;
                float multiplier = NextWaveCalculator();
                NextWave(multiplier);
            }
        }
    }


    /*
    //Returns number of enemies killed in the update.
    public int GetDeadEnemies()
    {
        int temp = numDeadEnemies;
        numDeadEnemies = 0;
        return temp;
    }
    */

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
        for (int i = 0; i < numEnemies; i++)
        {
            enemyList.get(i).draw(batch, parentAlpha);
        }
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(){
        paused = false;
    }

    public void LevelSelectWave(){
        currentWave = 10;
        waveToBeSpawnedNorm = 999;
        waveToBeSpawnedFast = 999;
        waveToBeSpawnedHeavy = 999;
    }
}
