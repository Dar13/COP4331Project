package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanner Foster on 2/9/2015. This will be our one thread to control
 * the updates of all the enemy types and all the enemies currently alive on the
 * board.
 */
public class EnemyManager implements Runnable
{
    private int NumNormEnemy;
    private int NumFastEnemy;
    private int NumHeavEnemy;
    private List<NormalEnemy> NormEnemy;
    //private List<FastEnemy> FastEnemyl;
    //private List<HeavEnemy> HeavyEnemy;


    public EnemyManager(int NumNormEnemy, int NumFastEnemy, int NumHeavEnemy)
    {
        this.NumNormEnemy = NumNormEnemy;
        this.NumFastEnemy = NumFastEnemy;
        this.NumHeavEnemy = NumHeavEnemy;
    }

    private void InitializeEnemyList(int NumNorm, int NumFast, int NumHeavy)
    {
        NormEnemy = new ArrayList<>();
        for (int i = 0; i != NumNorm; i++)
        {
            NormalEnemy Normalenemy = new NormalEnemy(0, 0, 'n');
            NormEnemy.add(Normalenemy);
        }

        /*
        // NOT IMPLEMENTED
        FastEnemyl = new ArrayList<>();
        for (int i = 0; i != NumNorm; i++)
        {
            FastEnemy Fastenemy = new FastEnemy(0, 0, 'n');
            FastEnemyl.add(Fastenemy);
        }

        HeavyEnemy = new ArrayList<>();
        for (int i = 0; i != NumNorm; i++)
        {
            HeavEnemy Heavyenemy = new HeavEnemy(0, 0, 'n');
            HeavyEnemy.add(Heavyenemy);
        }
        */
    }

    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            // wait a bit, to be adjusted to true update period.
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                break;
            }

            //Todo all the updating of the positions, health, and removal of enemies from the lists.

        }

    }

}
