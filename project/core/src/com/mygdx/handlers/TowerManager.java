package com.mygdx.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.entities.Tower;

import java.util.LinkedList;

/**
 * Created by LordNeah on 2/23/2015.
 */
public class TowerManager
{
    protected static final int MAGIC_NUMBER = 2;
    protected static final float MAGIC_NUMBER_2 = 1.25f;
    public LinkedList<Tower> towers;

    public TowerManager(LinkedList<Tower> towers)
    {
        this.towers = towers;
    }

    public void addTower(Texture img, float x, float y, float damage, float range)
    {
        Tower New = new Tower(img, x, y, damage, range);
        towers.addLast(New);
    }

    public void RenderAll(SpriteBatch sb)
    {
        for (int i = 0; i < towers.size(); i++)
        {
            towers.get(i).render(sb);
        }
    }

    public void upgradeTower(int towerToBeUpgraded)
    {
        towers.get(towerToBeUpgraded).damages = towers.get(towerToBeUpgraded).damages * MAGIC_NUMBER;
        towers.get(towerToBeUpgraded).range = towers.get(towerToBeUpgraded).range * MAGIC_NUMBER_2;
    }

}
