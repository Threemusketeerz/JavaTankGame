package context;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.*;
import java.awt.*;

public class Board extends JPanel
{
    private int x, y;
    private String mapFile;
    private BufferedImage loadedImage;
    private Image scaledImage;

    public Board(String mapFile)
    {
        x = 0;
        y = 0;
        this.mapFile = mapFile;
        loadImage(this.mapFile);
        setPreferredSize(new Dimension(getLoadedImage().getWidth(this), getLoadedImage().getHeight(this)));
        setVisible(true);
    }

    private void loadImage(String mapFile)
    {
        try
        {
            loadedImage = ImageIO.read(getClass().getResourceAsStream(mapFile));
            scaledImage = loadedImage.getScaledInstance(500, 500, 0);
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
    public int getW() { return loadedImage.getWidth(); }
    public int getH() { return loadedImage.getHeight(); }
    public BufferedImage getLoadedImage() { return loadedImage; }
}