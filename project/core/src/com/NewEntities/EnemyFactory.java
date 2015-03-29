package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by NeilMoore on 3/25/2015.
 */
public class EnemyFactory
{
    public static Enemy createEnemy(Entity.Type type, Texture north, Texture south, Texture east, Texture west,
                                    float x, float y)
    {
        switch(type)
        {
        case ENEMY_NORMAL:
            return new NormalEnemy(north, south, east, west, x, y);

        case ENEMY_FAST:
            return new FastEnemy(north, south, east, west, x, y);

        case ENEMY_HEAVY:
            return new HeavyEnemy(north, south, east, west, x, y);

        default:
            return null;
        }
    }
}
