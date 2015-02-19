package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.entities.Enemy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanner Foster on 2/9/2015. This will be our one thread to control
 * the updates of all the enemy types and all the enemies currently alive on the
 * board.
 */
public class EnemyManager
{
    private int NumNormEnemy;
    private int NumFastEnemy;
    private int NumHeavEnemy;
    float StartX;
    float StartY;
    private List<Enemy> NormEnemy;
    private Texture img;
    String DirectionStart;
    //private List<FastEnemy> FastEnemyl;
    //private List<HeavEnemy> HeavyEnemy;


    public EnemyManager(int NumNormEnemy, int NumFastEnemy, int NumHeavEnemy, float StartX, float StartY, String DirectionStart)
    {
        this.NumNormEnemy = NumNormEnemy;
        this.NumFastEnemy = NumFastEnemy;
        this.NumHeavEnemy = NumHeavEnemy;
        this.StartX = StartX;
        this.StartY = StartY;
        this.DirectionStart = DirectionStart;
        img = new Texture("EnemyDev.png");
    }

    private void InitializeEnemyList(int NumNorm, int NumFast, int NumHeavy)
    {
        NormEnemy = new ArrayList<>();
        for (int i = 0; i != NumNorm; i++)
        {
            Enemy NormalEnemy = new Enemy(img, StartX, StartY, 2, DirectionStart);
            NormEnemy.add(NormalEnemy);
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


    public void UpdateAll(float deltaTime) {
        for (int i = 0; i < NumNormEnemy; i++) {
            if (!NormEnemy.get(i).Check()) {
                NormEnemy.get(i).Move();
            }
        }
    }

}
