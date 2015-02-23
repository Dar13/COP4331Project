package com.mygdx.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.entities.Tower;

import java.util.LinkedList;

/**
 * Created by LordNeah on 2/23/2015.
 */
public class TowerManager {
    public LinkedList<Tower> towers;


    public TowerManager(LinkedList<Tower> towers){
        this.towers = towers;
    }

    public void addTower(Texture img, float x, float y, float damages, float range){
        Tower New = new Tower(img, x, y, damages, range);
        towers.addLast(New);
    }

    public void RenderAll(SpriteBatch sb){
        for (int i = 0; i < towers.size(); i++){
            towers.get(i).render(sb);
        }
    }

    public void upgradeTower(){

    }

}
