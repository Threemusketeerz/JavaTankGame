package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Map
{
    private String mapFile;
    private BufferedImage map;

    public Map(String mapFile)
    {
        this.mapFile = mapFile;
        loadImage(mapFile);

    }
    public BufferedImage getMap() { return map; }

    /**
     * Loads in our image from our resources.
     * @param mapFile Map to load in.
     */
    private void loadImage(String mapFile)
    {
        try
        {
            map = ImageIO.read(getClass().getResourceAsStream(mapFile));
            if (map == null)
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
