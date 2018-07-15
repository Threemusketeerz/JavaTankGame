
package model;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Tank extends Sprite implements Drawable
{
    // Orientation is for rotating the tank. It can be between 0 and 360, you know full circle;
    private BufferedImage       tankBase;
    private BufferedImage       tankCannon;
    private BufferedImage       bullet;
    private String              description;
    private Collision           collision;
    private Camera              camera;
    private boolean             drivingForwards, drivingBackwards;
    private boolean             shooting;
    // How long does the Tank have to wait before being able to shoot again?
    private long                rateOfFire;
    private long                lastFired;


    public Tank(BufferedImage tankBase, BufferedImage tankCannon, BufferedImage bullet,
                String description, double x, double y, double rotation,
                Constraint constraint, Collision collision)
    {
        super(x, y, rotation, constraint, collision);
        this.tankBase           = tankBase;
        this.tankCannon         = tankCannon;
        this.bullet             = bullet;
        this.description        = description;
        // Tanks don't start off drivingForwards or shooting
        this.drivingForwards    = false;
        this.drivingBackwards   = false;
        this.shooting           = false;

        this.rateOfFire         = 300;
        this.lastFired          = 0;
    }


    /**
     * If you wanna load in the assets a relative strings.
     * The base dir is /path/to/resources/folder/
     * @param tankBase
     * @param tankCannon
     * @param bulletType
     * @param description
     * @param x
     * @param y
     * @param rotation
     * @param constraint
     * @param collision
     */
    public Tank(String tankBase, String tankCannon, String bulletType,
                String description, double x, double y, double rotation,
                Constraint constraint, Collision collision)
    {
        this(loadImage(tankBase), loadImage(tankCannon), loadImage(bulletType), description, x, y, rotation, constraint, collision);
    }



    // Accessors
    public int              getWidth() { return tankBase.getWidth(); }
    public int              getHeight() { return tankBase.getHeight(); }
    @Override
    public BufferedImage    getImage() { return tankBase; }
    public BufferedImage    getTankBase() { return tankBase; }
    public BufferedImage    getTankCannon() { return tankCannon; }
    public long             getRateOfFire() { return rateOfFire; }
    public long             getLastFired() { return lastFired; }
    public Camera           getCamera() { return camera; }
    public BufferedImage    getBullet() { return bullet; }

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
