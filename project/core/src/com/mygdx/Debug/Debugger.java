package com.mygdx.Debug;

import com.NewEntities.NewTower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.entities.Enemy;
import com.mygdx.entities.Tower;
import com.mygdx.game.MyGame;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by James on 2/20/2015.
 */
public class Debugger
{
    public ShapeRenderer shapeRenderer;
    private LinkedList<WayPoint> path;
    private List<com.NewEntities.Tower> towers;
    private List<com.NewEntities.Enemy> enemies;
    Batch batch;
    private boolean finished = false;
    int waypointindex = 0;


    public Debugger(LinkedList<WayPoint> path, List<com.NewEntities.Tower> towers, List<com.NewEntities.Enemy> enemies, Batch batch)
    {
        this.batch = batch;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.path = path;
        this.towers = towers;
        this.enemies = enemies;
    }

    public void setBatch(Batch batch){
        this.batch = batch;
    }


    public void render()
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        /*Renders the waypoint paths with the path being represented by a
        line and the waypoint being represented by a square.
         */
        batch.begin();
        while (!finished)
        {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.line(path.get(waypointindex).x + 16, path.get(waypointindex).y + 16, path.get(waypointindex + 1).x + 16, path.get(waypointindex + 1).y + 16);
            shapeRenderer.rect(path.get(waypointindex).x, path.get(waypointindex).y, 32, 32);
            switch (path.get(waypointindex + 1).direction)
            {
            case END:
                finished = true;
                break;
            }
            waypointindex++;
        }

        /* Representing the tower range as a 32 pixel circle multiple centered at the center of the
        tower image.
         */
        int i = 0;
        while (i < towers.size())
        {
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.circle(towers.get(i).returnX() + 16, towers.get(i).returnY() + 16, towers.get(i).getRange() * 32);
            i++;
        }

        /* Representing the enemy hp as a green line, 6 pixels above the enemy. above the enemy
         */
        int j = 0;
        while (j < enemies.size())
        {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.line(enemies.get(j).returnX(), enemies.get(j).returnY() + 38, enemies.get(j).returnX() + enemies.get(j).getHealth() / 4, enemies.get(j).returnY() + 38);
            j++;
        }

        shapeRenderer.end();
        waypointindex = 0;
        finished = false;
        batch.end();
    }
}
