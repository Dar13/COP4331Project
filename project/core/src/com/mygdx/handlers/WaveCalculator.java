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

    public static float getDelay()
    {
        return (BASE_DELAY / (waveNumber * DELAY_MULTIPLIER));
    }

    public static int getAmountHeavyEnemies()
    {
        if(waveNumber < 10)
        {
            return 0;
        }
        else
        {
            return Math.round(waveNumber / 5);
        }
    }

    public static int getAmountFastEnemies()
    {
        if(waveNumber < 5)
        {
            return 0;
        }
        else
        {
            return Math.round(waveNumber * 2);
        }
    }

    public static int getAmountNormalEnemies()
    {
        int i = (int)Math.round(10 * Math.log((double)waveNumber * waveNumber)) + 5;
        //System.out.println("hi");
        return i;
    }

    public static float getMultiplierVelocity()
    {
        float multiplierV = (waveNumber * .05f);
        return multiplierV;
    }

    public static float getMultiplierArmor()
    {
        float multiplierA = (waveNumber * .05f);
        return multiplierA;
    }

    public static float getMultiplierSpawnRate(int gold)
    {
        float multiplierSp = (waveNumber * .5f) + (gold * .001f);
        return multiplierSp;
    }
}
