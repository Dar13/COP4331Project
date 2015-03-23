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
    protected static final int upgradeDamageConstatnt = 2;
    protected static final float upgradeRangeConstant = 1.25f;
    protected static final int baseRifleRange = 2;
    protected static final int baseRifleDamage = 1;
    protected static final int baseBazookaRange = 2;
    protected static final int baseBazookaDamage = 4;
    protected static final int baseSniperRange = 5;
    protected static final int baseSniperDamage = 5;
    public int rifleBasePrice = 100;
    public int bazookaBasePrice = 150;
    public int sniperBasePrice = 300;


    Texture RifleTower = new Texture("RifleTower.png");
    Texture BazookaTower = new Texture("BazookaTower.png");
    Texture TowerShadow = new Texture("shadowtower.png");

    public LinkedList<Tower> towers;

    public TowerManager(LinkedList<Tower> towers)
    {
        this.towers = towers;
    }

    //Adds new rifle tower to the tower linked list.
    public void addRifleTower(float x, float y)
    {
        Tower New = new Tower(RifleTower, TowerShadow, x, y, baseRifleDamage, baseRifleRange, Tower.Type.RIFLE);
        towers.addLast(New);
    }

    //Adds new bazooka tower to the tower linked list.
    public void addBazookaTower(float x, float y)
    {
        Tower New = new Tower(BazookaTower, TowerShadow, x, y, baseBazookaDamage, baseBazookaRange, Tower.Type.BAZOOKA);
        towers.addLast(New);
    }

    //Adds new sniper tower to the tower linked list.
    public void addSniperTower(float x, float y)
    {
        Tower New = new Tower(RifleTower, TowerShadow, x, y, baseSniperDamage, baseSniperRange, Tower.Type.SNIPER);
        towers.addLast(New);
    }

    public void RenderAll(SpriteBatch sb)
    {
        for (int i = 0; i < towers.size(); i++)
        {
            towers.get(i).render(sb);
        }
    }

    //Upgrades selected tower based on upgrade constants.
    public void upgradeTower(int towerToBeUpgraded)
    {
        towers.get(towerToBeUpgraded).damages = towers.get(towerToBeUpgraded).damages * upgradeDamageConstatnt;
        towers.get(towerToBeUpgraded).range = towers.get(towerToBeUpgraded).range * upgradeRangeConstant;
    }

}
