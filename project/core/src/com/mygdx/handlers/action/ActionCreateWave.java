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

    public ActionCreateWave(int waveNumber)
    {
        super();

        this.waveNumber = waveNumber;

        WaveCalculator.setWaveNumber(waveNumber);
        armorMultiplier = WaveCalculator.getArmorMultiplier();
        speedMultiplier = WaveCalculator.getSpeedMultiplier();
        delay = WaveCalculator.getDelay();
    }
}
