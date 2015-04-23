package com.mygdx.handlers.action;

import com.mygdx.handlers.WaveCalculator;

/**
 * Created by Neil on 4/7/2015.
 */
public class ActionCreateWave extends Action
{
    public int waveNumber;
    public float armorMultiplier;
    public float delay;
    public float speedMultiplier;

    public int amountHeavyEnemies;
    public int amountFastEnemies;
    public int amountNormalEnemies;
    public int amountTotalEnemies;

    public ActionCreateWave()
    {

    }

    public ActionCreateWave(int waveNumber)
    {
        super();

        actionClass = ActionClass.ACTION_CREATE_WAVE;

        this.waveNumber = waveNumber;

        WaveCalculator.setWaveNumber(waveNumber);
        armorMultiplier = WaveCalculator.getArmorMultiplier();
        speedMultiplier = WaveCalculator.getSpeedMultiplier();
        delay = WaveCalculator.getDelay();

        amountHeavyEnemies = WaveCalculator.getAmountHeavyEnemies();
        amountFastEnemies = WaveCalculator.getAmountFastEnemies();
        amountNormalEnemies = WaveCalculator.getAmountNormalEnemies();

        amountTotalEnemies = amountFastEnemies + amountHeavyEnemies + amountNormalEnemies;

        System.out.println("[WAVE] Total enemies: " + amountTotalEnemies);
    }
}
