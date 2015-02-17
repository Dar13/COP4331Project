package com.mygdx.game;

/**
 * Created by Tanner Foster on 2/8/2015.
 * Normal Enemy class to point to the abstract enemy class, nothing much here yet.
 */
public class NormalEnemy extends AbstractEnemy {

    public NormalEnemy(int XCoord, int YCoord, char Direction){
        super("Normal", XCoord, YCoord, Direction);
    }

}
