package view;

import manager.GameManager;
import model.Map;
import model.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameView extends JPanel implements ActionListener
{
    private int                 mapWidth;
    private int                 mapHeight;
    private final int           DELAY = 5;
    private Timer               timer;
    private Map                 map;
    private Sprite              player;
    private ArrayList<Sprite>   sprites;
    private GameManager         gameManager;

    public GameView()
    {
        //player = new Sprite();

        setVisible(true);

        timer = new Timer(DELAY, this);
        timer.start();
        gameManager = new GameManager();

        sprites = new ArrayList<>();
        map = new Map("/map2.png");

        mapWidth = map.getMap().getWidth();
        mapHeight = map.getMap().getHeight();

        player = new Sprite("/Tank/PNG/Tanks/tankBlue.png", "Player 1", mapWidth/2, mapHeight/2);

        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);

        setPreferredSize(new Dimension(mapWidth, mapHeight));
    }

    public int getMapWidth() { return mapWidth; }
    public int getMapHeight() { return mapHeight; }

    public void update()
    {
        repaint();
    }

    public void paintComponent(Graphics g)
    {

        Graphics2D g2d = (Graphics2D) g;

        g.drawImage(map.getMap(), 0, 0, null);
        // Draw player
        g2d.drawImage(player.getLoadedImage(), player.getAffineTransform(), null);

        // Draw players from server.
//        for (Sprite sprite : sprites)
//        {
//            g2d.drawImage(sprite.getLoadedImage(), sprite.getAffineTransform(), null);
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        gameManager.move(player);
//        for (Sprite sprite : sprites)
//            gameManager.move(sprite);
        update();
    }

    public class TAdapter extends KeyAdapter
    {
        public void keyPressed(KeyEvent e)
        {
            gameManager.keyPressed(player, e);
        }

        public void keyReleased(KeyEvent e)
        {
            gameManager.keyReleased(player, e);
        }
    }

    public static void main (String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension windowSize = new Dimension();
                JFrame frame = new JFrame();
                frame.setContentPane(new GameView());
//                frame.setLocation((int)screenSize.getWidth()/2 - mapWidth/2, (int)screenSize.getHeight()/2 - mapHeight/2);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                windowSize.setSize(screenSize.getWidth()/2 - frame.getWidth()/2, screenSize.getHeight()/2 - frame.getHeight()/2);
                frame.setLocation((int)windowSize.getWidth(), (int)windowSize.getHeight());
                frame.setVisible(true);
            }
        });
    }

}
