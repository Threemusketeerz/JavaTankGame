package view;

import manager.GameManager;
import model.*;

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
    private Tank                player;
    private ArrayList<Tank>     tanks;
    private ArrayList<Bullet>   bullets;
    private GameManager         gameManager;

    public GameView()
    {
        //player = new Tank();

        setVisible(true);

        gameManager = new GameManager();

        tanks = new ArrayList<>();
        bullets = BulletContainer.getInstance().getBullets();

        // Create map.
        MapContainer.getInstance().setMap(new Map("/map2.png"));
        // Assign it.
        map = MapContainer.getInstance().getMap();

        mapWidth = map.getMap().getWidth();
        mapHeight = map.getMap().getHeight();

        player = new Tank("/Tank/PNG/Tanks/tankBlue.png",
                "/Tank/PNG/Tanks/barrelBlue.png",
                "/Tank/PNG/Bullets/bulletBlue.png",
                "Player 1",
                mapWidth / 2, mapHeight / 2, 0.0,
                new Constraint(0, 0, mapWidth, mapHeight),
                null);
        player.setConstraint(new Constraint(0, 0, mapWidth, mapHeight));

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

        drawPlayer(g2d, player);
        for (Bullet bullet : bullets)
        {
            drawBullet(g2d, player, bullet);
        }

        // Draw players from server.
//        for (Tank tank : tanks)
//        {
//            drawPlayer(g2d, tank);
//        }

        g2d.dispose();
    }

    /**
     * Draws tank with rotation, this is both the cannon and the tankbase.
     * @param g2d       Graphics context to draw in
     * @param tank      Tank to draw
     */
    public void drawPlayer(Graphics2D g2d, Tank tank)
    {
        // Draws the tank base.
        drawDrawable(g2d, tank);

        // Draws cannon onto tank
        // This is a bit finicky. This should probably also be put into the drawable. Nevertheless
        // we split it up for now.
        // TODO: Make the cannon drawable
        BufferedImage cannonImage = tank.getTankCannon();
        // Sets the location of the image, we want it in the middle of the tank.
        AffineTransform cannonAt = AffineTransform.getTranslateInstance(tank.getX() - cannonImage.getWidth()/2, tank.getY() - cannonImage.getHeight());
        // Calculates rotation. We want the rotation to happen at the bottom of the picture, ergo
        // .getHeight is not divided by 2
        cannonAt.rotate(Math.toRadians(tank.getRotation()), cannonImage.getWidth()/2, cannonImage.getHeight());

        g2d.drawImage(tank.getTankCannon(), cannonAt, null);
    }

    public void drawDrawable(Graphics2D g2d, Drawable drawable)
    {
        AffineTransform baseAt = AffineTransform.getTranslateInstance(drawable.getX() - drawable.getWidth()/2, drawable.getY() - drawable.getHeight()/2);
        baseAt.rotate(Math.toRadians(drawable.getRotation()), drawable.getWidth()/2, drawable.getHeight()/2);

        g2d.drawImage(drawable.getImage(), baseAt, null);

    }

    public void drawBullet(Graphics2D g2d, Tank tank, Bullet bullet)
    {
        AffineTransform baseAt = AffineTransform.getTranslateInstance(tank.getX() - bullet.getWidth()/2, tank.getY() - bullet.getHeight());
        baseAt.rotate(Math.toRadians(bullet.getRotation()), bullet.getWidth()/2, bullet.getHeight()/2);

        g2d.drawImage(bullet.getImage(), baseAt, null);

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        gameManager.moveTank(player);
        for (Bullet bullet : BulletContainer.getInstance().getBullets())
        {
            gameManager.move(bullet);
        }
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
