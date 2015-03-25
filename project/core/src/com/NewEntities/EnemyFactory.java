package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public class EnemyFactory
{
    public static Enemy createEnemy(Entity.Type type, Texture baseTexture, Texture otherTexture,
                                    float x, float y)
    {
        switch(type)
        {
        case ENEMY_NORMAL:
            return new NormalEnemy(baseTexture, otherTexture, x, y);

        case ENEMY_FAST:
            return new FastEnemy(baseTexture, otherTexture, x, y);

        case ENEMY_HEAVY:
            return new HeavyEnemy(baseTexture, otherTexture, x, y);

        default:
            return null;
        }
    }
}
