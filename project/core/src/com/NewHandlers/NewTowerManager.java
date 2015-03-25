package com.NewHandlers;

import com.NewEntities.NewTower;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.LinkedList;

/**
 * Created by James on 3/20/2015.
 */
public class NewTowerManager extends Actor {
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

    public LinkedList<NewTower> towers;

    public NewTowerManager(LinkedList<NewTower> towers)
    {
        this.towers = towers;
    }

    //Adds new rifle tower to the tower linked list.
    public void addRifleTower(float x, float y)
    {
        System.out.println("addRifleTower");
        NewTower New = new NewTower(RifleTower, TowerShadow, x, y, baseRifleDamage, baseRifleRange, NewTower.Type.TOWER_RIFLE);
        towers.addLast(New);
    }

    //Adds new bazooka tower to the tower linked list.
    public void addBazookaTower(float x, float y)
    {
        NewTower New = new NewTower(BazookaTower, TowerShadow, x, y, baseBazookaDamage, baseBazookaRange, NewTower.Type.TOWER_BAZOOKA);
        towers.addLast(New);
    }

    //Adds new sniper tower to the tower linked list.
    public void addSniperTower(float x, float y)
    {
        NewTower New = new NewTower(RifleTower, TowerShadow, x, y, baseSniperDamage, baseSniperRange, NewTower.Type.TOWER_SNIPER);
        towers.addLast(New);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < towers.size(); i++)
        {
            towers.get(i).draw(batch,parentAlpha);
        }
    }


    //Upgrades selected tower based on upgrade constants.
    public void upgradeTower(int towerToBeUpgraded)
    {
        towers.get(towerToBeUpgraded).damages = towers.get(towerToBeUpgraded).damages * upgradeDamageConstatnt;
        towers.get(towerToBeUpgraded).range = towers.get(towerToBeUpgraded).range * upgradeRangeConstant;

    }

}
