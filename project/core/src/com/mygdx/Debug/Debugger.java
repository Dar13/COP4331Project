package com.mygdx.Debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.entities.Enemy;
import com.mygdx.entities.Tower;
import com.mygdx.game.MyGame;
import com.mygdx.triggers.WayPoint;

import java.util.LinkedList;

/**
 * Created by James on 2/20/2015.
 */
public class Debugger
{
    public OrthographicCamera debugeCam;
    public ShapeRenderer shapeRenderer;
    private LinkedList<WayPoint> path;
    private LinkedList<Tower> towers;
    private LinkedList<Enemy> enemies;
    private boolean finished = false;
    int waypointindex = 0;


    public Debugger(LinkedList<WayPoint> path, LinkedList<Tower> towers, LinkedList<Enemy> enemies)
    {
        debugeCam = new OrthographicCamera();
        debugeCam.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(debugeCam.combined);
        this.path = path;
        this.towers = towers;
        this.enemies = enemies;
    }

    public void render()
    {
        debugeCam.update();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        /*Renders the waypoint paths with the path being represented by a
        line and the waypoint being represented by a square.
         */
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
            shapeRenderer.circle(towers.get(i).x + 16, towers.get(i).y + 16, towers.get(i).range * 32);
            i++;
        }

        /* Representing the enemy hp as a green line, 6 pixels above the enemy. above the enemy
         */
        int j = 0;
        while (j < enemies.size())
        {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.line(enemies.get(j).sprite.getX(), enemies.get(j).sprite.getY() + 38, enemies.get(j).sprite.getX() + enemies.get(j).health / 4, enemies.get(j).sprite.getY() + 38);
            j++;
        }

        shapeRenderer.end();
        waypointindex = 0;
        finished = false;
    }
}
