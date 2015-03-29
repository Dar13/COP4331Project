package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public class TowerFactory
{
    public static Tower createTower(Entity.Type type, Texture baseTexture, Texture otherTexture,
                                    float x, float y)
    {
        switch(type)
        {
        case TOWER_RIFLE:
            return new RifleTower(baseTexture, otherTexture, x, y);

        case TOWER_BAZOOKA:
            return new BazookaTower(baseTexture, otherTexture, x, y);

        case TOWER_SNIPER:
            return new SniperTower(baseTexture, otherTexture, x, y);

        case TOWER_MORTAR:
            return new MortarTower(baseTexture, otherTexture, x, y);

        default:
            return null;
        }
    }
}
