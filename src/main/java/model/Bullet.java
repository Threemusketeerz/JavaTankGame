package model;

import java.awt.image.BufferedImage;

public class Bullet extends Sprite implements Drawable
{
    private static int          uid = 0;
    private static final int    SPEED = 10;

    private int                 id;
    private BufferedImage       image;
    private boolean             initialState;

    // Which player do I belong to?
    private Tank                tank;

    public Bullet(Tank tank, String imagePath, double x, double y, double rotation, Constraint constraint, Collision collision)
    {
        super(x, y, rotation, constraint, collision);
        this.image      = loadImage(imagePath);
        this.tank       = tank;
        initialState = true;
        id = uid;
        uid++;
    }

    public int getId() { return id; }
    public int getWidth() { return image.getWidth(); }
    public int getHeight() { return image.getHeight(); }
    public BufferedImage getImage() { return image; }
    public Sprite getTank() { return tank; }
    public void setTank(Tank tank) { this.tank = tank; }

    public double getSpeed()
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
