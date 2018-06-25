package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Sprite
{
    private double      x, dx, y, dy, rotation, deltaRotation;
    private Constraint  constraint;
    private Collision   collision;

    public Sprite(double x, double y, double rotation, Constraint constraint, Collision collision)
   {
        this.x          = x;
        this.y          = y;
        this.rotation   = rotation;
        this.constraint = constraint;
        this.collision  = collision;
    }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }
    public void setDeltaRotation(int deltaRotation) { this.deltaRotation = deltaRotation; }
    public void setBufferRotation(double rotation)
    {
        if (this.rotation > 360)
            this.rotation = 0;
        if (this.rotation < 0)
            this.rotation = 360;
        this.rotation = rotation;
    }
    public void setConstraint(Constraint constraint) { this.constraint = constraint; }
    public void setCollision(Collision collision) { this.collision = collision; }



    public double getX() { return x; }
    public double getY() { return y; }
    public double getRotation() { return rotation; }
    public double getDeltaRotation() { return deltaRotation; }
    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public Constraint getConstraint() { return this.constraint; }
    public Collision getCollision() { return collision; }

    public static BufferedImage loadImage(String imagePath)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(Sprite.class.getClass().getResourceAsStream(imagePath));
            if (image == null)
            {
                System.out.println("Couldn't find " + imagePath);
            }
        }
        catch(IOException ioe)
        {
            System.out.println("Failed to load map!");
        }
        return image;
    }
}
