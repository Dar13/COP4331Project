package com.mygdx.handlers;

/**
 * Created by Neil on 4/7/2015.
 */
public class WaveCalculator
{
    public static final float BASE_DELAY = 1.0f;
    public static final float DELAY_MULTIPLIER = .75f;

    protected static float waveNumber = 1;

    public static void setWaveNumber(int number)
    {
        waveNumber = number;
    }

    public static float getArmorMultiplier()
    {
        return ((waveNumber * waveNumber) / 1000.0f);
    }

    public static float getSpeedMultiplier()
    {
        return 1.0f + ((waveNumber * waveNumber) / 2500);
    }

    public static float getDelayMultiplier()
    {
        return (BASE_DELAY / (waveNumber * DELAY_MULTIPLIER));
    }
}
