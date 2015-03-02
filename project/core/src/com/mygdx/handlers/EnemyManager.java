package com.mygdx.handlers;

import com.badlogic.gdx.graphics.Texture;
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
    protected static final int MYSTICAL_MAGIC_NUMBER = 16;
    protected static final int MYSTICAL_MAGIC_NUMBER_2 = 32;

    public int numEnemies;
    public int numHEnemies;
    public Texture img;
    public LinkedList<Enemy> enemies;
    public LinkedList<EnemyHeavy> enemyHeavies;
    public LinkedList<Tower> towers;


    public EnemyManager(int numEnemies, int numHEnemies)
    {
        enemies = new LinkedList<Enemy>();
        enemyHeavies = new LinkedList<EnemyHeavy>();
        towers = new LinkedList<Tower>();
        img = new Texture("EnemyDev.png");
        this.numEnemies = numEnemies;
    }

    public void AddEnemy(Texture img, float velocity, float armor, LinkedList<WayPoint> path)
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

    public void UpdateAll(float deltaTime, LinkedList<Tower> towers)
    {
        this.towers = towers;

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
        if (Math.abs(enemyX + MYSTICAL_MAGIC_NUMBER - towerX + MYSTICAL_MAGIC_NUMBER)
                < towerRange * MYSTICAL_MAGIC_NUMBER_2
                && Math.abs(enemyY + MYSTICAL_MAGIC_NUMBER - towerY + MYSTICAL_MAGIC_NUMBER)
                < towerRange * MYSTICAL_MAGIC_NUMBER_2)
        {
            if (Math.sqrt(Math.pow((enemyX + MYSTICAL_MAGIC_NUMBER - towerX + MYSTICAL_MAGIC_NUMBER), 2)
                                  + Math.pow((enemyY + MYSTICAL_MAGIC_NUMBER - towerY + MYSTICAL_MAGIC_NUMBER), 2))
                    < towerRange * MYSTICAL_MAGIC_NUMBER_2)
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

