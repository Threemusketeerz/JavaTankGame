package states;

import engine.GameState;
import manager.BulletManager;
import manager.GameManager;
import manager.TankManager;
import model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import parser.Spritesheet;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import view.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

public class PlayState implements GameState
{
    private static PlayState    instance = new PlayState();

    private GameManager         gameManager;
    private TankManager         tankManager;
    private BulletManager       bulletManager;
    private ArrayList<Bullet>   bullets;
    private ArrayList<Bullet>   garbage;
    private Tank                player;
    private TankMap             map;
    private Spritesheet         spritesheet;

    private PlayState()
    {
    }

    @Override
    public void init(final int screenWidth, final int screenHeight)
    {
        gameManager = new GameManager();
        tankManager = new TankManager();
        bulletManager = new BulletManager();
        bullets = new ArrayList<>();
        garbage = new ArrayList<>();


        // TODO FIX Ugly try catch
        try
        {
            map = new TankMap(getClass().getResource("/Maps/RenderTesters2.json").toString().replace("file:", ""));
            MapContainer.getInstance().setMap(map);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        spritesheet = new Spritesheet("/Tank/Spritesheet/sheet_tanks.png");

        player = new Tank(tankManager.findAsset("tankBlue_outline.png"),
                tankManager.findAsset("barrelBlue_outline.png"),
                bulletManager.findAsset("bulletBlue_outline.png"),
                "Player 1",
                map.getWidthInPixels()/2, map.getHeightInPixels()/ 2, 0.0f,
                new Constraint(0, 0, map.getWidthInPixels(), map.getHeightInPixels()),
                null);
        tankManager.attachCamera(player, new Camera(new Point(player.getX() - screenWidth/2, player.getY() - screenHeight/2)));
    }

    public static PlayState getInstance()
    {
        if (instance == null)
            instance = new PlayState();

        return instance;
    }

    @Override
    public void cleanup()
    {
        throw new NotImplementedException();
    }

    @Override
    public void pause()
    {
        throw new NotImplementedException();
    }

    @Override
    public void resume()
    {
        throw new NotImplementedException();
    }

    @Override
    public void handleEvents(ActionEvent e)
    {
        update();
    }

    // TODO: Reevaluate whether to keep key presses in Controller layer. We might want his this somewhere else.
    public void keyPressed( KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (key)
        {
            case KeyEvent.VK_UP:
                tankManager.isDrivingForwards(player, true);
                break;
            case KeyEvent.VK_RIGHT:
                tankManager.rotateRight(player);
                break;
            case KeyEvent.VK_DOWN:
                tankManager.isDrivingBackwards(player, true);
                break;
            case KeyEvent.VK_LEFT:
                tankManager.rotateLeft(player);
                break;
            case KeyEvent.VK_SPACE:
                tankManager.shoot(player);
                tankManager.shooting(player,true);
                break;
        }
    }

    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (key)
        {
            case KeyEvent.VK_UP:
                tankManager.stop(player);
                break;
            case KeyEvent.VK_RIGHT:
                tankManager.stopTurningRight(player);
                break;
            case KeyEvent.VK_DOWN:
                tankManager.stop(player);
                break;
            case KeyEvent.VK_LEFT:
                tankManager.stopTurningLeft(player);
                break;
            case KeyEvent.VK_SPACE:
                tankManager.shooting(player,false);
        }
    }

    @Override
    public void update()
    {
        tankManager.moveTank(player);
        tankManager.shoot(player);

        for (Bullet bullet : BulletContainer.getInstance().getBullets())
        {
            gameManager.move(bullet);
        }
    }

    @Override
    public void draw(Graphics2D g2d)
    {

        // Draw map
        drawMap(g2d, player);
        drawBullets(g2d);
        drawPlayer(g2d, player);
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

    /**
     * Handles logic for drawing bullets.
     * It also makes sure the bullets gets disposed if put in garbage
     * @param g2d    Context to draw in.
     */
    public void drawBullets(Graphics2D g2d)
    {

        ListIterator<Bullet> it = bullets.listIterator();

        while (it.hasNext())
        {
            Bullet bullet = it.next();
            drawBullet(g2d, player, bullet);
////            System.out.println("-----------------------------");
////            System.out.println("bulletId: " + bullet.getId());
////            System.out.println("x: " + bullet.getX());
////            System.out.println("y: " + bullet.getY());
        }

        disposeBullets(garbage);

    }

    /**
     * Draw logic for a single bullet.
     * @param g2d       Context to draw in
     * @param tank      If it is first time the bullet is drawn, we want the orientation of the tank.
     * @param bullet    Bullet to draw.
     */
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

        // Check state of bullet
        gameManager.checkBulletConstraints(bullet, garbage);

    }

    /**
     * Draws the map and calculates a tile area to render.
     * If the tile area of the screen is 4x4 tiles, this methods makes it 6x6, to optimize rendering.
     * @param g2d
     * @param tank
     */
    public void drawMap(Graphics2D g2d, Tank tank)
    {
//        final Graphics2D g2d = (Graphics2D) g.create();
        Rectangle clip = new Rectangle(Game.WIDTH, Game.HEIGHT);

//        if (g2d == null)
//            System.err.println("G2d is null");
//        if (clip == null)
//            System.err.println("Clip is null");

        // Draw a gray background
        g2d.setPaint(new Color(100, 100, 100));
//
        g2d.fill(clip);
        // Draw each map layer
        int layerCount = map.getLayers().size();

        // Relocate draw location, to fit start of the draw point.
        int xOffset = (int)tank.getCamera().getOffset().getX();
        int yOffset = (int)tank.getCamera().getOffset().getY();

        // Draw from
        double tileOffset = 1 * map.getTileWidth();
        double xCamera = tank.getCamera().getOffset().getX();
        double yCamera = tank.getCamera().getOffset().getY();

        // Draws entire map.
        for (int i = 0; i < layerCount; i++)
        {
            JSONObject layer = (JSONObject) map.getLayers().get(i);
//            long[] data = (long[]) layer.get("data");
            JSONArray data = (JSONArray) layer.get("data");

            ArrayList<BufferedImage> tilesToDraw = new ArrayList<>();

            // Gather all the tiles we want drawn.
            for (int j = 0; j < data.size(); j++)
            {
                // Width in pixels
                int mapWidth = map.getWidth() * map.getTileWidth();

                // Height in pixels
                int mapHeight = map.getHeight() * map.getTileHeight();

                // Current j position, mapped to pixels
                int pointInPixelsX = j * map.getTileWidth();
                int pointInPixelsY = j * map.getTileHeight();

                // Position x and y for drawing tile calculated with pixels
                int posX = pointInPixelsX % mapWidth;
                int posY = (pointInPixelsY / mapHeight) * map.getTileHeight();

                // Tile to fetch
                int dataPoint = (int)((long)data.get(j));

                // Data to fetch the rendering area, should be a square around the player position
                // TODO Switch the tileOffset to - -> + and vice versa
                if (clip == null)
                {
                    System.out.println("Tank is null");
                }
                double drawStartPointX = (tank.getX() - clip.getWidth()/2) - tileOffset;
                double drawStartPointY = (tank.getY() - clip.getHeight()/2) - tileOffset;
                double drawEndPointX = (xCamera + clip.getWidth()) + tileOffset;
                double drawEndPointY = (yCamera + clip.getHeight()) + tileOffset;

                if (posX >= drawStartPointX
                        && posY >= drawStartPointY
                        && posX <= drawEndPointX
                        && posY <= drawEndPointY)
                {
                    // Actual tile.
                    // TODO fix hardcoded values.
                    BufferedImage tile = spritesheet.getTileAt(
                            map.getTileWidth(), map.getTileHeight(),
                            // Spritesheet width and height. Not to be confused with the Tilesheet
                            8, 4,
                            dataPoint);

                    g2d.drawImage(tile, posX - xOffset, posY - yOffset, null);
                }

            }
        }
    }

    public void disposeBullets(ArrayList<Bullet> garbage)
    {
        for (Bullet bullet : garbage)
        {
            bullets.remove(bullet);
        }
    }
}
