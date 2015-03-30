package com.NewEntities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by LordNeah on 3/29/2015.
 */
public class MortarTower extends Tower {

        public static final float BASE_DAMAGE = 4.0f;
        public static final float BASE_RANGE = 30.0f;
        public static final int PRICE = 300;
        public static final float FIRING_DELAY = 30f;

        public MortarTower(Texture baseTexture, Texture otherTexture, float x, float y, Stage stage)
        {
            super(Type.TOWER_MORTAR, baseTexture, otherTexture, x, y, stage);

            damage = BASE_DAMAGE;
            range = BASE_RANGE;
            price = PRICE;
            firingDelay = FIRING_DELAY;
        }

        @Override
        public void draw(Batch batch, float parentAlpha)
        {
            other.draw(batch);
            base.draw(batch);
        }
}
