package model;

import java.awt.image.BufferedImage;

public class Bullet extends Sprite implements Drawable
{
    private final int           SPEED = 10;
    private BufferedImage image;

    // Which player do I belong to?
    private Tank                tank;
    private double              speed;

    public Bullet(Tank tank, String imagePath, double x, double y, double rotation, Constraint constraint, Collision collision)
    {
        super(x, y, rotation, constraint, collision);
        this.image      = loadImage(imagePath);
        this.tank       = tank;
    }

    public int getWidth() { return image.getWidth(); }
    public int getHeight() { return image.getHeight(); }
    public BufferedImage getImage() { return image; }
    public Sprite getTank() { return tank; }
    public void setTank(Tank tank) { this.tank = tank; }

    public double getSpeed()
    {
        return speed;
    }
}
