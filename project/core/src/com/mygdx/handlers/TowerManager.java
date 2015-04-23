package com.mygdx.handlers;

import com.mygdx.entities.Entity;
import com.mygdx.entities.Tower;
import com.mygdx.entities.TowerFactory;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 3/20/2015.
 */
public class TowerManager extends Actor
{
    public static final String rifleTexturePath = "Towers/RifleBunker.png";
    public static final String bazookaTexturePath = "Towers/BazookaBunker.png";
    public static final String sniperTexturePath = "Towers/SniperBunker.png";
    public static final String mortarTexturePath = "Towers/Mortarteam.png";
    public static final String shadowTexturePath = "Towers/nulllayer.png";

    protected static final int upgradeDamageConstant = 2;
    protected static final float upgradeRangeConstant = 1.25f;

    public List<Tower> towerList;
    protected Map<Entity.Type, Texture> textureMap;
    protected Texture towerShadow;

    protected int idCounter = 0;

    //public NewTowerManager(LinkedList<NewTower> towers)
    public TowerManager(List<Tower> towers)
    {
        //this.towers = towers;
        towerList = towers;

        textureMap = new HashMap<>();
        textureMap.put(Entity.Type.TOWER_RIFLE, new Texture(rifleTexturePath));
        textureMap.put(Entity.Type.TOWER_BAZOOKA, new Texture(bazookaTexturePath));
        textureMap.put(Entity.Type.TOWER_SNIPER, new Texture(sniperTexturePath));
        textureMap.put(Entity.Type.TOWER_MORTAR, new Texture(mortarTexturePath));

        towerShadow = new Texture(shadowTexturePath);
    }

    public void addTower(Entity.Type type, float x, float y)
    {
        Tower tower = TowerFactory.createTower(type, textureMap.get(type), towerShadow, x - 16, y - 16, getStage());

        tower.entityID = idCounter;
        idCounter++;

        towerList.add(tower);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        for(Tower tower : towerList)
        {
            tower.draw(batch, parentAlpha);
        }
    }

    public boolean onAnotherTower(float x, float y){

        for(int i = 0; i < towerList.size(); i++)
        {
            if(towerList.get(i).steppingOnToes(x, y)){
                return true;
            }
        }

        return false;
    }

    public int towerAct(float delta, int gold)
    {
        int gold1 = 0;
        for(Tower tower : towerList)
        {
            gold1 += tower.buttonAct(delta, gold);
        }
        return gold1;
    }

    //Upgrades selected tower based on upgrade constants.
    public void upgradeTower(int towerID)
    {
        // maybe reconsider this. We do want to upgrade based on entityID rather than a list index however.
        for(Tower tower : towerList)
        {
            if(tower.entityID == towerID)
            {
                tower.upgrade(upgradeDamageConstant, upgradeRangeConstant);
            }
        }
    }

}
