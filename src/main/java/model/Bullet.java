package model;

import org.newdawn.slick.Image;

import java.awt.image.BufferedImage;

public class Bullet extends Sprite implements Drawable
{
    public static final float   SPEED = .1f;
    // Let bullet be alive for 3 seconds.
    public static final long    LIFE_TIME = 1000;

    private static int          uid = 0;

    private int                 id;
    private Image               image;
    private boolean             initialState;

    private boolean             hasExploded;

    // When did the bullet spawn
    private long                spawnTime;
    // How long will it live
    private long                livedTime;

    // Which player do I belong to?
    private Tank                tank;

    public Bullet(Tank tank, Image image, float x, float y, float rotation, Constraint constraint, Collision collision)
    {
        super(x, y, rotation, constraint, collision);
        this.image      = image;
        this.tank       = tank;

        this.spawnTime  = System.currentTimeMillis();
        this.livedTime  = 0;

        initialState = true;
        hasExploded = false;
        id = uid;
        uid++;

    }

    public int getId() { return id; }
    public int getWidth() { return image.getWidth(); }
    public int getHeight() { return image.getHeight(); }
    public float getCenterX() { return getX() + getImage().getWidth()/2; }
    public float getCenterY() { return getY() + getImage().getHeight()/2; }
    public Image getImage() { return image; }
    public Tank getTank() { return tank; }
    public boolean hasExploded()
    {
        return hasExploded;
    }

    public long getSpawnTime()
    {
        return spawnTime;
    }

    public long getLivedTime()
    {
        return livedTime;
    }

    public void setTank(Tank tank) { this.tank = tank; }

    public float getSpeed()
    {
        return SPEED;
    }

    public boolean isInitialState()
    {
        return initialState;
    }

    public void setInitialState(boolean initialState)
    {
        this.initialState = initialState;
    }

    public void explode()
    {
        hasExploded = true;
    }
}
