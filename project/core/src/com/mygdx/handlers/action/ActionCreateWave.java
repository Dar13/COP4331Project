package com.mygdx.handlers.action;

import com.mygdx.handlers.WaveCalculator;

/**
 * Created by Neil on 4/7/2015.
 */
public class ActionCreateWave extends Action
{
    public int waveNumber;
    public float armorMultiplier;
    public float delayMultiplier;
    public float speedMultiplier;

    public ActionCreateWave(int waveNumber)
    {
        super();

        this.waveNumber = waveNumber;

        WaveCalculator.setWaveNumber(waveNumber);
        armorMultiplier = WaveCalculator.getArmorMultiplier();
        speedMultiplier = WaveCalculator.getSpeedMultiplier();
        delayMultiplier = WaveCalculator.getDelayMultiplier();
    }
}
