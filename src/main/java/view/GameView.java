package view;

import manager.BulletManager;
import manager.GameManager;
import manager.TankManager;
import model.*;
import util.Point;
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
    public static final int     WIDTH = 1200, HEIGHT = 800;
    // Framerate. Essentially, 1 second = 1000ms / FRAME_RATE = DELAY.
    public static final int     FRAME_RATE = 60;
    private static final int    DELAY = 1000/FRAME_RATE;
    private int                 mapWidth;
    private int                 mapHeight;
    private int                 count = 0;
    private Timer               timer;
    private TankMap             map;
    private MapView             mapView;
    private Tank                player;
    private ArrayList<Tank>     tanks;
    private ArrayList<Bullet>   bullets;
    private GameManager         gameManager;
    private TankManager         tankManager;
    private BulletManager       bulletManager;

    public GameView()
    {
        //player = new Tank();

        setVisible(true);

        // Generic setup
        gameManager = new GameManager();
        tankManager = new TankManager();
        bulletManager = new BulletManager();

        tanks = new ArrayList<>();
        bullets = BulletContainer.getInstance().getBullets();

        // load the layers
        try
        {
            // Load the json file to read data from. Replace "file:"
            map = new TankMap(getClass().getResource("/Maps/RenderTesters2.json").toString().replace("file:", ""));
            Spritesheet spriteSheet = new Spritesheet("/Tank/Spritesheet/sheet_tanks.png");

            mapView = new MapView(map, spriteSheet);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
//        mapView = new MapView(map.getMap());
        // Create map.
        // TODO STATE PATTERN!!!!!
        MapContainer.getInstance().setMap(map);
        // Assign it.
//        map = MapContainer.getInstance().getMap();

        mapWidth = map.getWidthInPixels();
        mapHeight = map.getHeightInPixels();

        player = new Tank(tankManager.findAsset("tankBlue_outline.png"),
                tankManager.findAsset("barrelBlue_outline.png"),
                bulletManager.findAsset("bulletBlue_outline.png"),
                "Player 1",
//                map.getWidthInPixels()/2, map.getHeightInPixels()/ 2, 0.0,
                WIDTH/2, HEIGHT/2, 0.0,
                new Constraint(0, 0, mapWidth, mapHeight),
                null);
        // GameManager
        tankManager.attachCamera(player, new Camera(new Point(player.getX() - WIDTH/2, player.getY() - HEIGHT/2)));


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

    // Essentially or render() method
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        ArrayList<Bullet> garbage = new ArrayList<>();

        // Draw map
        mapView.paintComponent(g2d, player);

        ListIterator<Bullet> it = bullets.listIterator();

        while (it.hasNext())
        {
            Bullet bullet = it.next();
            drawBullet(g2d, player, bullet);
            gameManager.checkBulletConstraints(bullet, garbage);
////            System.out.println("-----------------------------");
////            System.out.println("bulletId: " + bullet.getId());
////            System.out.println("x: " + bullet.getX());
////            System.out.println("y: " + bullet.getY());
        }
//

        drawPlayer(g2d, player);

//        System.out.println("Player X: " + player.getX());
//        System.out.println("Player Y: " + player.getY());

        disposeBullets(garbage);

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
//        drawDrawable(g2d, tank);
        // We need to draw the tank with the camera offset.
        AffineTransform baseAt = AffineTransform.getTranslateInstance(
                (tank.getX() - tank.getWidth()/2) - tank.getCamera().getOffset().getX(),
                (tank.getY() - tank.getHeight()/2) - tank.getCamera().getOffset().getY());

        baseAt.rotate(Math.toRadians(tank.getRotation()), tank.getWidth()/2, tank.getHeight()/2);

        g2d.drawImage(tank.getImage(), baseAt, null);

        // Draws cannon onto tank
        // This is a bit finicky. This should probably also be put into the drawable. Nevertheless
        // we split it up for now.
        // TODO: Make the cannon drawable
        BufferedImage cannonImage = tank.getTankCannon();
        // Sets the location of the image, we want it in the middle of the tank.
        AffineTransform cannonAt = AffineTransform.getTranslateInstance((tank.getX() - cannonImage.getWidth()/2) - tank.getCamera().getOffset().getX(),
                (tank.getY() - cannonImage.getHeight()) - tank.getCamera().getOffset().getY());
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
            double xOffset = tank.getCamera().getOffset().getX();
            double yOffset = tank.getCamera().getOffset().getY();
            double xSpawn = tank.getX() - widthDiff - xOffset;
            double ySpawn = tank.getY() - heightDiff - yOffset;

            baseAt = AffineTransform.getTranslateInstance(xSpawn  - (bullet.getWidth() / 2), ySpawn - (bullet.getHeight() / 2));

            // Big nono, we want a manager to handle this.
//            bullet.setX(xSpawn);
//            bullet.setY(ySpawn);
            gameManager.updateBulletLocation(bullet, new Point(xSpawn, ySpawn));
            gameManager.updateBulletInitialState(bullet,false);
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
//            System.out.println("-----------------------------");
//            System.out.println("bulletId: " + bullet.getId());
//            System.out.println("x: " + bullet.getX());
//            System.out.println("y: " + bullet.getY());
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
        EventQueue.invokeLater(() ->
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
        });
    }

}
