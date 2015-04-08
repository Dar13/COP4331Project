package com.mygdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.handlers.EnemyManager;
import com.mygdx.states.GameState;
import com.mygdx.states.Play;


/**
 * Created by NeilMoore on 3/25/2015.
 */
public abstract class Tower extends Entity
{
    public static final int MAX_LEVEL = 6;

    // gameplay attributes
    protected float damage;
    protected float range;
    protected int price;
    protected float firingDelay;
    protected float timeSinceLastShot;

    // display attributes
    private boolean clicked = false;
    protected Sprite base;
    protected Sprite other;
    protected int target;
    protected int towerLevel;
    protected float targetDistanceTraveled;

    public static final float DAMAGE_MULTIPLIER_BAZOOKA = 1.50f;
    public static final float DAMAGE_MULTIPLIER_RIFLE = 1.65f;
    public static final float DAMAGE_MULTIPLIER_SNIPER = 1.25f;
    public static final float DAMAGE_MULTIPLIER_MORTAR = 1.50f;

    public static final float RANGE_MULTIPLIER_BAZOOKA = 1.15f;
    public static final float RANGE_MULTIPLIER_RIFLE = 1.15f;
    public static final float RANGE_MULTIPLIER_SNIPER = 1.25f;
    public static final float RANGE_MULTIPLIER_MORTAR = 1.0f;



    private TextButton upgradeButton;
    private TextButton cancelButton;
    Skin skin = new Skin(Gdx.files.internal("UiData/uiskin.json"));

    public Tower(Type type, Texture baseTexture, Texture otherTexture, float x, float y, Stage stage)
    {
        super(x, y);
        final Stage STage = stage;
        towerLevel = 1;

        this.type = type;
        this.timeSinceLastShot = 0;
        targetDistanceTraveled = 0;

        base = new Sprite(baseTexture);
        base.setPosition(x, y);

        other = new Sprite(otherTexture);
        other.setPosition(x + 9, y - 23);
        other.rotate(-45);

        upgradeButton = new TextButton("upgrade", skin);
        upgradeButton.setSize(64, 16);
        upgradeButton.setPosition(base.getX() + 40, base.getY() + 32);

        cancelButton = new TextButton("cancel", skin);
        cancelButton.setSize(64, 16);
        cancelButton.setPosition(upgradeButton.getX(), upgradeButton.getY() - 24);
        cancelButton.setDisabled(true);
        cancelButton.setVisible(false);
        cancelButton.setChecked(false);
        upgradeButton.setDisabled(true);
        upgradeButton.setVisible(false);
        upgradeButton.setChecked(false);
        STage.addActor(upgradeButton);
        STage.addActor(cancelButton);

        Image imageActor = new Image(otherTexture);
        imageActor.setPosition(x, y);
        imageActor.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Clicked");
                cancelButton.setDisabled(false);
                cancelButton.setVisible(true);
                upgradeButton.setDisabled(false);
                upgradeButton.setVisible(true);
                clicked = true;
            }

        });
        STage.addActor(imageActor);

    }

    public abstract void draw(Batch batch, float parentAlpha);

    public float getTimeSinceLastShot()
    {
        return timeSinceLastShot;
    }

    public int buttonAct(float delta, int gold)
    {
        if (clicked) {
            upgradeButton.act(delta);
            cancelButton.act(delta);
            if (upgradeButton.isChecked())
            {
                int gold1 = 0;
                switch (this.type) {
                    case TOWER_BAZOOKA:
                        if (gold > towerLevel * BazookaTower.PRICE) {
                            gold1 = towerLevel * BazookaTower.PRICE;
                            upgrade(DAMAGE_MULTIPLIER_BAZOOKA, RANGE_MULTIPLIER_BAZOOKA);
                        }
                        break;
                    case TOWER_RIFLE:
                        if (gold > towerLevel * RifleTower.PRICE) {
                            gold1 = towerLevel * RifleTower.PRICE;
                            upgrade(DAMAGE_MULTIPLIER_RIFLE, RANGE_MULTIPLIER_RIFLE);
                        }
                        break;
                    case TOWER_SNIPER:
                        if (gold > towerLevel * SniperTower.PRICE) {
                            gold1 = towerLevel * SniperTower.PRICE;
                            upgrade(DAMAGE_MULTIPLIER_SNIPER, RANGE_MULTIPLIER_SNIPER);
                        }
                        break;
                    case TOWER_MORTAR:
                        if (gold > towerLevel * MortarTower.PRICE) {
                            gold1 = towerLevel * MortarTower.PRICE;
                            upgrade(DAMAGE_MULTIPLIER_MORTAR, RANGE_MULTIPLIER_MORTAR);
                        }
                        break;
                }
                cancelButton.setDisabled(true);
                cancelButton.setVisible(false);
                cancelButton.setChecked(false);
                upgradeButton.setDisabled(true);
                upgradeButton.setVisible(false);
                upgradeButton.setChecked(false);

                clicked = false;
                return gold1;
            }

            else if (cancelButton.isChecked())
            {
                cancelButton.setDisabled(true);
                cancelButton.setVisible(false);
                cancelButton.setChecked(false);
                upgradeButton.setDisabled(true);
                upgradeButton.setVisible(false);
                upgradeButton.setChecked(false);
                clicked = false;
                return 0;
            }
        }
        return 0;
    }

    public boolean readyToFire(){
        if(timeSinceLastShot > firingDelay){
            // Is this ok?
            //resetTimeSinceLastShot();
            return true;
        }

        return false;
    }

    public void updateTimeSinceLastShot()
    {
        timeSinceLastShot++;
    }

    public void resetTimeSinceLastShot()
    {
        timeSinceLastShot = 0;
    }

    public int getTarget()
    {
        return target;
    }

    public void setTarget(int targetID)
    {
        this.target = targetID;
    }

    public int getTowerLevel()
    {
        return towerLevel;
    }

    public void setTargetDistanceTraveled(float targetDistanceTraveled)
    {
        this.targetDistanceTraveled = targetDistanceTraveled;
    }

    public float getTargetDistanceTraveled()
    {
        return targetDistanceTraveled;
    }

    public float getDamage(Type type)
    {
        switch (this.type)
        {
        case TOWER_BAZOOKA:
            switch (type)
            {
            case ENEMY_NORMAL:
                return (damage / 3);
            case ENEMY_FAST:
                return (damage / 3);
            case ENEMY_HEAVY:
                return (damage * 2);
            }
            break;

        case TOWER_RIFLE:
            switch (type)
            {
            case ENEMY_NORMAL:
                return damage;
            case ENEMY_FAST:
                return damage;
            case ENEMY_HEAVY:
                return (damage / 3);
            }
            break;

        case TOWER_SNIPER:
            switch (type)
            {
            case ENEMY_NORMAL:
                return (damage * 2);
            case ENEMY_FAST:
                return damage;
            case ENEMY_HEAVY:
                return (damage / 2);
            }
            break;
        case TOWER_MORTAR:
            switch (type)
            {
            case ENEMY_NORMAL:
                return damage;
            case ENEMY_FAST:
                return damage;
            case ENEMY_HEAVY:
                return (damage / 2);
            }
            break;
        default:
            return damage;
        }

        return damage;
    }

    public float getRange()
    {
        return range;
    }

    public int getPrice()
    {
        return price;
    }

    public boolean inRange(float x, float y, int positionOffset, int rangeOffset)
    {
        return inRange(new Vector2(x,y),positionOffset, rangeOffset);
    }

    public boolean inRange(Vector2 position, int positionOffset, int rangeOffset)
    {
        Vector2 ePosition = new Vector2(position);
        ePosition.x += positionOffset;
        ePosition.y += positionOffset;

        Vector2 tPosition = new Vector2(this.position);
        tPosition.x += positionOffset;
        tPosition.y += positionOffset;

        float nRange = range * rangeOffset;

        return (tPosition.sub(ePosition).len2() <= (nRange * nRange));
    }

    //Checks to see if the x, y coordinates input clash with the stored x, y coordinates.
    public boolean steppingOnToes(float x, float y)
    {
        Rectangle bounds = new Rectangle(base.getX(), base.getY(), 32, 32);
        Rectangle rect = new Rectangle(x, y, 32, 32);

        return bounds.overlaps(rect);
    }

    public void upgrade(float damageMultiplier, float rangeMultiplier)
    {
        damage *= damageMultiplier;
        range *= rangeMultiplier;
        towerLevel++;
    }
}
