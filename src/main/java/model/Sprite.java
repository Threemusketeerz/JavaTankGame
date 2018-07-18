package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Sprite
{
    private float x, dx, y, dy, rotation, deltaRotation;
    private Constraint  constraint;
    private Collision   collision;

    public Sprite(float x, float y, float rotation, Constraint constraint, Collision collision)
   {
        this.x          = x;
        this.y          = y;
        this.rotation   = rotation;
        this.constraint = constraint;
        this.collision  = collision;
    }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setDx(float dx) { this.dx = dx; }
    public void setDy(float dy) { this.dy = dy; }
    public void setDeltaRotation(int deltaRotation) { this.deltaRotation = deltaRotation; }
    public void setRotation(float rotation)
    {
        if (this.rotation > 360)
            this.rotation = 0;
        if (this.rotation < 0)
            this.rotation = 360;
        this.rotation = rotation;
    }
    public void setConstraint(Constraint constraint) { this.constraint = constraint; }
    public void setCollision(Collision collision) { this.collision = collision; }



    public float getX() { return x; }
    public float getY() { return y; }
    public float getRotation() { return rotation; }
    public float getDeltaRotation() { return deltaRotation; }
    public float getDx() { return dx; }
    public float getDy() { return dy; }
    public Constraint getConstraint() { return constraint; }
    public Collision getCollision() { return collision; }

}
