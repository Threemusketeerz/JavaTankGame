
package model;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Sprite
{
    // Orientation is for rotating the tank. It can be between 0 and 360, you know full circle;
    private double              x, dx, y, dy;
    private int                 width, height, rotateDirection;
    private double              bufferRotation, deltaRotation;
    private BufferedImage       tankBase;
    private BufferedImage       tankCannon;
    private String              description;
    private boolean             driving;


    public Sprite(String tankBase, String tankCannon, String description, double x, double y, double rotation)
    {
        this.tankBase           = loadImage(tankBase);
        this.tankCannon         = loadImage(tankCannon) ;
        this.description        = description;
        this.x                  = x;
        this.y                  = y;
        this.width              = getTankBase().getWidth();
        this.height             = getTankBase().getHeight();
        this.bufferRotation     = rotation;
        this.rotateDirection    = 0;
        this.driving            = false;
    }


    public Sprite(String image, String description, int x, int y)
    {
        this(image, null, description, x, y, 0.0);
    }

    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }
    public void setBufferRotation(double bufferRotation)
    {
        if (bufferRotation > 360)
            bufferRotation = 0;
        if (bufferRotation < 0)
            bufferRotation = 360;
        this.bufferRotation = bufferRotation;
    }
    public void setIsDriving(boolean state) { this.driving = state; }
    public void setDeltaRotation(int deltaRotation) { this.deltaRotation = deltaRotation; }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getBufferRotation() { return bufferRotation; }
    public BufferedImage getTankBase() { return tankBase; }
    public BufferedImage getTankCannon() { return tankCannon; }
    public double getDeltaRotation() { return deltaRotation; }
    public double getDx() { return dx; }
    public void setX(double x) { this.x = x; }
    public double getDy() { return dy; }

    private BufferedImage loadImage(String imagePath)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
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

    public void setY(double y)
    {
        this.y = y;
    }

    public boolean isDriving()
    {
        return driving;
    }
}
