package view;

import manager.GameManager;
import model.Constraint;
import model.Map;
import model.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameView extends JPanel implements ActionListener
{
    private int                 mapWidth;
    private int                 mapHeight;
    private final int           DELAY = 20;
    private Timer               timer;
    private Map                 map;
    private Tank player;
    private ArrayList<Tank> tanks;
    private GameManager         gameManager;

    public GameView()
    {
        //player = new Tank();

        setVisible(true);

        gameManager = new GameManager();

        tanks = new ArrayList<>();
        map = new Map("/map2.png");

        mapWidth = map.getMap().getWidth();
        mapHeight = map.getMap().getHeight();

        player = new Tank("/Tank/PNG/Tanks/tankBlue.png", "/Tank/PNG/Tanks/barrelBlue.png", "/Tank/PNG/Tanks/bulletBlue.png", "Player 1", mapWidth / 2, mapHeight / 2, 0.0, new Constraint(0, 0, map.getMap().getWidth(), map.getMap().getHeight()), null);

        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);

        setPreferredSize(new Dimension(mapWidth, mapHeight));

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public int getMapWidth()
    {
        return mapWidth;
    }

    public int getMapHeight()
    {
        return mapHeight;
    }

    public void update()
    {
        repaint();
    }

    public void paintComponent(Graphics g)
    {

        Graphics2D g2d = (Graphics2D) g.create();
        // Draw map
        g.drawImage(map.getMap(), 0, 0, null);
        g2d.dispose();

        g2d = (Graphics2D) g.create();
        drawPlayer(g2d, player);
        g2d.dispose();

        // Draw players from server.
//        for (Tank sprite : tanks)
//        {
//            g2d.drawImage(sprite.getLoadedImage(), sprite.getAffineTransform(), null);
//        }
    }

    public void drawPlayer(Graphics2D g2d, Tank player)
    {
        // Draw player
        // Draws the tank base.
        AffineTransform baseAt = AffineTransform.getTranslateInstance(player.getX() - player.getWidth()/2, player.getY() - player.getHeight()/2);
        baseAt.rotate(Math.toRadians(player.getBufferRotation()), player.getWidth()/2, player.getHeight()/2);

        // Draws cannon onto tank
        BufferedImage cannonImage = player.getTankCannon();
        // Sets the location of the image, we want it in the middle of the tank.
        AffineTransform cannonAt = AffineTransform.getTranslateInstance(player.getX() - cannonImage.getWidth()/2, player.getY() - cannonImage.getHeight());
        // Calculates rotation. We want the rotation to happen at the bottom of the picture, ergo
        // .getHeight is not divided by 2
        cannonAt.rotate(Math.toRadians(player.getBufferRotation()), cannonImage.getWidth()/2, cannonImage.getHeight());

        g2d.drawImage(player.getTankBase(), baseAt,null);
        g2d.drawImage(player.getTankCannon(), cannonAt, null);
    }

    public void shoot(Graphics2D g2d, Tank player)
    {


    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        gameManager.move(player);
//        for (Tank sprite : tanks)
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

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Point windowLocation = new Point();
                JFrame frame = new JFrame();
                frame.setContentPane(new GameView());
//                frame.setLocation((int)screenSize.getWidth()/2 - mapWidth/2, (int)screenSize.getHeight()/2 - mapHeight/2);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
//                windowLocation.setLocation(screenSize.getWidth() / 2 - frame.getWidth() / 2, screenSize.getHeight() / 2 - frame.getHeight() / 2);
//                frame.setLocation((int) windowLocation.getX(), (int) windowLocation.getY());
                frame.setVisible(true);
            }
        });
    }

}
