package view;

import manager.GameManager;
import model.*;
import model.Point;
import model.TankMap;
import parser.Spritesheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.ListIterator;

public class GameView extends JPanel implements ActionListener
{
    private int                 mapWidth;
    private int                 mapHeight;
    private int                 count = 0;
    private final int           DELAY = 20;
    private Timer               timer;
    private TankMap             map;
    private MapView             mapView;
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

        File testFile = new File(getClass().getResource("/Tank/Spritesheet/sheet_tanks.png").toString().replace("file:", ""));

        if (testFile.exists())
            System.out.println(testFile.toString() + " exists!!!!!");

        // load the layers
        try
        {
            // Load the json file to read data from. Replace "file:"
            TankMap file = new TankMap(getClass().getResource("/Maps/RenderTesters2.json").toString().replace("file:", ""));
//            Spritesheet spriteSheet = new Spritesheet(getClass().getResource("Tank/Spritesheet/sheet_tanks.png").toString().replace("file:", ""));
            Spritesheet spriteSheet = new Spritesheet("/Tank/Spritesheet/sheet_tanks.png");

            mapView = new MapView(file, spriteSheet);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(800, 600));
//        mapView = new MapView(map.getMap());
        // Create map.
//        MapContainer.getInstance().setMap(new TankMap("/map2.png"));
        // Assign it.
//        map = MapContainer.getInstance().getMap();

//        mapWidth = map.getMap().getWidth();
//        mapHeight = map.getMap().getHeight();

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

//        setPreferredSize(new Dimension(mapWidth, mapHeight));

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
        mapView.paintComponent(g2d);
//        g.drawImage(map.getMap(), 0, 0, null);
        //        for (Bullet bullet : bullets)
//        {
//            drawBullet(g2d, player, bullet);
//        }

//        ListIterator<Bullet> it = bullets.listIterator();
//        ArrayList<Bullet> garbage = new ArrayList<>();
//
//        while (it.hasNext())
//        {
//            Bullet bullet = it.next();
//            drawBullet(g2d, player, bullet);
//            gameManager.checkBulletConstraints(bullet, garbage);
////            System.out.println("-----------------------------");
////            System.out.println("bulletId: " + bullet.getId());
////            System.out.println("x: " + bullet.getX());
////            System.out.println("y: " + bullet.getY());
//        }
//
//        disposeBullets(garbage);
//        drawPlayer(g2d, player);
        // Draw players from server.
//        for (Tank tank : tanks)
//        {
//            drawPlayer(g2d, tank);
//        }

        g2d.dispose();
    }

    public void disposeBullets(ArrayList<Bullet> garbage)
    {
        for (Bullet bullet : garbage)
        {
            bullets.remove(bullet);
        }
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
        AffineTransform baseAt = AffineTransform.getTranslateInstance(drawable.getX() - drawable.getWidth()/2,
                                                                      drawable.getY() - drawable.getHeight()/2);

        baseAt.rotate(Math.toRadians(drawable.getRotation()), drawable.getWidth()/2, drawable.getHeight()/2);

        g2d.drawImage(drawable.getImage(), baseAt, null);

    }
    // Draws bullet initially
    // We need a bullet drawer that does it after the
    public void drawBullet(Graphics2D g2d, Tank tank, Bullet bullet)
    {

        // We want to rotate around the base.
        // We need to find the x and y of the base. Since we've coupled the cannon on, this calculation
        // is necessary to achieve wanted result
        AffineTransform baseAt = null;
        // Sets the x to the center of the tankBase, which is our spawnPoint
        if (bullet.isInitialState())
        {
            int heightDiff = tank.getHeight() - tank.getTankBase().getHeight();
            int widthDiff = tank.getWidth() - tank.getTankBase().getWidth();
            double spawnX = tank.getX() - widthDiff;
            double spawnY = tank.getY() - heightDiff;

            baseAt = AffineTransform.getTranslateInstance(spawnX - (bullet.getWidth() / 2), spawnY - (bullet.getHeight() / 2));
            bullet.setX(spawnX);
            bullet.setY(spawnY);
            gameManager.updateBulletLocation(bullet, new Point(spawnX, spawnY));
            gameManager.bulletUpdateInitialState(bullet,false);
        }
        else
        {
            baseAt = AffineTransform.getTranslateInstance(bullet.getX() - (bullet.getWidth() / 2), bullet.getY() - (bullet.getHeight() / 2));
        }

        // Orients the bullet around this point
        baseAt.rotate(Math.toRadians(bullet.getRotation()), bullet.getWidth()/2, bullet.getHeight()/2);
        g2d.drawImage(bullet.getImage(), baseAt, null);

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        gameManager.moveTank(player);
        gameManager.shoot(player);

        for (Bullet bullet : BulletContainer.getInstance().getBullets())
        {
            System.out.println("-----------------------------");
            System.out.println("bulletId: " + bullet.getId());
            System.out.println("x: " + bullet.getX());
            System.out.println("y: " + bullet.getY());
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
                //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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
