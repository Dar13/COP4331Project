package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public class TowerFactory
{
    public static Tower createTower(Entity.Type type, Texture baseTexture, Texture otherTexture,
                                    float x, float y, Stage stage)
    {
        switch(type)
        {
        case TOWER_RIFLE:
            return new RifleTower(baseTexture, otherTexture, x, y, stage);

        case TOWER_BAZOOKA:
            return new BazookaTower(baseTexture, otherTexture, x, y, stage);

        case TOWER_SNIPER:
            return new SniperTower(baseTexture, otherTexture, x, y, stage);

        case TOWER_MORTAR:
            return new MortarTower(baseTexture, otherTexture, x, y, stage);

        default:
            return null;
        }
    }
}
