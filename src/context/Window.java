package context;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame
{
    private final int WIDTH = 800, HEIGHT = 600;
    private Tilemap map;

    public Window()
    {
        //player = new Sprite();
        map = new Tilemap("/map2.png");
        setContentPane(map);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(map.getLoadedImage().getWidth(), map.getLoadedImage().getHeight()));
        pack();
        setVisible(true);
    }
}
