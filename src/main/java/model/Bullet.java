package model;

import org.newdawn.slick.Image;

import java.awt.image.BufferedImage;

public class Bullet extends Sprite implements Drawable
{
    private static int          uid = 0;
    private static final int    SPEED = 10;

    // Let bullet be alive for 3 seconds.
    private static final int    LIFE_TIME = 3000;

    private int                 id;
    private Image               image;
    private boolean             initialState;
    private int

    // Which player do I belong to?
    private Tank                tank;

    public Bullet(Tank tank, Image image, float x, float y, float rotation, Constraint constraint, Collision collision)
    {
        super(x, y, rotation, constraint, collision);
        this.image      = image;
        this.tank       = tank;
        initialState = true;
        id = uid;
        uid++;

    }

    public int getId() { return id; }
    public int getWidth() { return image.getWidth(); }
    public int getHeight() { return image.getHeight(); }
    public Image getImage() { return image; }
    public Tank getTank() { return tank; }
    public void setTank(Tank tank) { this.tank = tank; }

    public int getSpeed()
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
}
