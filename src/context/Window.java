package context;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame
{
    private final int WIDTH = 800, HEIGHT = 600;

    private Board board;
    // player?
    private Sprite player;

    public Window()
    {
        //player = new Sprite();
        initWindowWithView("Sprite");
        JsonObject
    }

    private void initWindowWithView(String title)
    {
        JPanel gamePanel = player.getView();
        gamePanel.setLayout(null);
        gamePanel.setLocation(0, 0);
        add(gamePanel);

        pack();

        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initWindow(String title)
    {
        JPanel board = new Board("")
    }

}
