
package model;
import java.awt.image.BufferedImage;

public class Tank extends Sprite implements Drawable
{
    // Orientation is for rotating the tank. It can be between 0 and 360, you know full circle;
    private String              bulletType;
    private BufferedImage       tankBase;
    private BufferedImage       tankCannon;
    private String              description;
    private Collision           collision;
    private boolean             drivingForwards, drivingBackwards;
    private boolean             shooting;
    // How long does the Tank have to wait before being able to shoot again?
    private long                rateOfFire;
    private long                lastFired;




    public Tank(String tankBase, String tankCannon, String bulletType, String description, double x, double y, double rotation, Constraint constraint, Collision collision)
    {
        super(x, y, rotation, constraint, collision);
        this.tankBase           = loadImage(tankBase);
        this.tankCannon         = loadImage(tankCannon);
        this.bulletType         = bulletType;
        this.description        = description;
        // Tanks don't start off drivingForwards or shooting
        this.drivingForwards    = false;
        this.drivingBackwards   = false;
        this.shooting           = false;

        this.rateOfFire         = 300;
        this.lastFired          = 0;
    }

    // Accessors
    public int getWidth() { return tankBase.getWidth(); }
    public int getHeight() { return tankBase.getHeight(); }
    @Override
    public BufferedImage getImage() { return tankBase; }
    public String getBulletType() { return bulletType; }
    public BufferedImage getTankBase() { return tankBase; }
    public BufferedImage getTankCannon() { return tankCannon; }
    public long getRateOfFire() { return rateOfFire; }
    public long getLastFired() { return lastFired; }
    public boolean isDrivingForwards() { return drivingForwards; }
    public boolean isShooting() { return shooting; }

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
}
