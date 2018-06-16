
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
    private int x, dx, y, dy, width, height;
    private double rotation;
    private static final int SPEED = 1;
    private BufferedImage tankBase;
    private BufferedImage tankCannon;
    private String description;

    public Sprite(String image, String description, int x, int y, int width, int height, double rotation)
    {
        loadImage(image);
        this.description = description;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = 0.0;
    }


    public Sprite(String image, String description, int x, int y)
    {
        this(image, description, x, y, 100, 100, 0.0);
    }

    public void setDx(int dx) { this.dx = dx; }
    public void setDy(int dy) { this.dy = dy; }

    public AffineTransform getAffineTransform()
    {
       Graphics2D g2d = (Graphics2D) tankBase.getGraphics();
       return g2d.getTransform();
    }



//    public void move()
//    {
//        x += dx;
//        y += dy;
//    }

    public void move()
    {
        y += dy;
    }


    public int getX() { return x; }
    public int getY() { return y; }
    public BufferedImage getLoadedImage() { return tankBase; }

    private void loadImage(String imagePath)
    {
        try
        {
            tankBase = ImageIO.read(getClass().getResourceAsStream(imagePath));
            if (tankBase == null)
            {
                System.out.println("Crying");
            }
        }
        catch(IOException ioe)
        {
            System.out.println("Failed to load map!");
        }
    }
}
