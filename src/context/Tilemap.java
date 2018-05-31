package context;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.*;
import java.awt.*;

/**
 * Tilemap will be a picture of a map to be used as a map for all players.
 * This will eventually, if possible be replaced by a more sophisticated way of
 * rendering maps. Such as Tile rendering.
 */
public class Tilemap extends JPanel
{
    private int x, y;
    private String mapFile;
    private BufferedImage loadedImage;
    private Image scaledImage;

    public Tilemap(String mapFile)
    {
        x = 0;
        y = 0;
        this.mapFile = mapFile;
        loadImage(mapFile);
        // setPreferredSize(new Dimension(getLoadedImage().getWidth(this), getLoadedImage().getHeight(this)));
        setVisible(true);
    }

    /**
     * Loads in our image from our resources.
     * @param mapFile Map to load in.
     */
    private void loadImage(String mapFile)
    {
        try
        {
            loadedImage = ImageIO.read(getClass().getResourceAsStream(mapFile));
            if (loadedImage == null)
            {
                System.out.println("Crying");
            }
            // scaledImage = loadedImage.getScaledInstance(500, 500, 0);
        }
        catch(IOException ioe)
        {
            System.out.println("Failed to load map!");
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
//        g.setColor(new Color(100, 100, 100));
//        g.fillRect(x, y, 1000, 1000);
        g.drawImage(loadedImage, x, y, null);
    }

    public int getX() { return x;  }
    public int getY() { return y; }
    public int getWidth() { return loadedImage.getWidth(); }
    public int getHeight() { return loadedImage.getHeight(); }
    public BufferedImage getLoadedImage() { return loadedImage; }
}
