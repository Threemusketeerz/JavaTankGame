
package model;
import java.awt.image.BufferedImage;

import org.newdawn.slick.Image;

public class Tank extends Sprite implements Drawable
{
    // Orientation is for rotating the tank. It can be between 0 and 360, you know full circle;
    private Image               tankBase;
    private Image               bullet;
    private String              description;
    private Collision           collision;
    private Camera              camera;
    private boolean             drivingForwards, drivingBackwards;
    private boolean             shooting;
    // How long does the Tank have to wait before being able to shoot again?
    private long                rateOfFire;
    private long                lastFired;
    private float               speed;


    public Tank(Image tankBase, Image bullet,
                String description, float x, float y, float rotation,
                Constraint constraint, Collision collision)
    {
        super(x, y, rotation, constraint, collision);
        this.tankBase           = tankBase;
        this.bullet             = bullet;
        this.description        = description;
        // Tanks don't start off drivingForwards or shooting
        this.drivingForwards    = false;
        this.drivingBackwards   = false;
        this.shooting           = false;

        this.rateOfFire         = 300;
        this.lastFired          = 0;
        this.speed              = .2f;
    }


    // Accessors
    public int              getWidth() { return tankBase.getWidth(); }
    public int              getHeight() { return tankBase.getHeight(); }
    @Override
    public Image            getImage() { return tankBase; }
    public Image            getTankBase() { return tankBase; }
    public Image            getBullet() { return bullet; }
    public long             getRateOfFire() { return rateOfFire; }
    public long             getLastFired() { return lastFired; }
    public Camera           getCamera() { return camera; }
    public float            getSpeed() { return speed; }

    public boolean          isDrivingForwards() { return drivingForwards; }
    public boolean          isShooting() { return shooting; }

    // Mutators
    public void setIsDrivingForwards(boolean state)
    {
        drivingBackwards = false;
        this.drivingForwards = state;
    }
    public void shooting(boolean state) { this.shooting = state; }
    public void setRateOfFire(long rateOfFire) { this.rateOfFire = rateOfFire; }
    public void setLastFired(long lastFired) { this.lastFired = lastFired; }

    public void setIsDrivingBackwards(boolean isDrivingBackwards)
    {
        drivingForwards = false;
        this.drivingBackwards = isDrivingBackwards;
    }

    public boolean isDrivingBackwards()
    {
        return drivingBackwards;
    }

    public void setCamera(Camera camera) { this.camera = camera; }

}
