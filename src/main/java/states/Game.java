package states;


import engine.Engine;
import manager.BulletManager;
import manager.GameManager;
import manager.TankManager;
import model.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import util.Key;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game extends BasicGameState
{
    public static final int ID = 0;

    private TiledMap            map;
    private Tank                tank;
    private ArrayList<Bullet>   bullets;
    private ArrayList<Bullet>   garbage;
    private TankManager         tankManager;
    private BulletManager       bulletManager;
    private GameManager         gameManager;


    @Override
    public int getID()
    {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException
    {
        // Setup instances
        tankManager = new TankManager();
        bulletManager = new BulletManager();
        gameManager = new GameManager();
        bullets = BulletContainer.getInstance().getBullets();
        garbage = new ArrayList<>();
        map = new TiledMap("Maps/Map01.tmx");
        MapContainer.getInstance().setMap(map);

        int mapWidth = map.getWidth() * map.getTileWidth();
        int mapHeight = map.getWidth() * map.getTileHeight();

        System.out.println("Available assets");
        for (String key : TankContainer.getInstance().keySet())
        {
            System.out.println(key);
        }

        if (tankManager.findAsset("tankBlue_outline.png") == null)
        {
            System.err.println("Couldn't find asset");
        }

        tank = new Tank(tankManager.findAsset("tankBlue_outline.png"),
                tankManager.findAsset("barrelBlue_outline.png"),
                bulletManager.findAsset("bulletBlue_outline.png"),
                "Player 1",
                mapWidth/2, mapHeight/2, 0f,
                new Constraint(0, 0, mapWidth, mapHeight),
                null
        );

        tankManager.attachCamera(tank, new Camera(new Point(tank.getX() - Engine.WIDTH/2, tank.getY() - Engine.HEIGHT/2)));
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException
    {
        float xOffset = tank.getCamera().getOffset().getX();
        float yOffset = tank.getCamera().getOffset().getY();
        map.render(-(int)xOffset, -(int)yOffset);

        // Render bullets first, so they appear to go through the tank.
        renderBullets();
        renderTank(xOffset, yOffset);
    }

    public void renderTank(float xOffset, float yOffset)
    {
        float xTank = tank.getX() - xOffset;
        float yTank = tank.getY() - yOffset;

        Image tankBase = tank.getTankBase();
        Image tankCannon = tank.getTankCannon();

        // Padding for positioning the cannon.
        float padding = 10.0f;

        // Draw base
        tankBase.setRotation(tank.getRotation());
//        tank.getImage().setCenterOfRotation(tankBase.getWidth()/2, tankBase.getHeight()/2);
        tankBase.draw(xTank - tankBase.getWidth()/2, yTank - tankBase.getHeight()/2);

        // Draw cannon. The 10 in the end is a sort of padding to center the cannon of the drawing
        tankCannon.setCenterOfRotation(tankCannon.getWidth()/2, tankCannon.getHeight() - padding);
        tankCannon.setRotation(tank.getRotation());
//        tank.getTankCannon().rotate(tank.getRotation());
        tankCannon.draw(xTank - tankCannon.getWidth()/2, yTank - tankBase.getHeight()/2 - padding);
    }

    public void renderBullets()
    {
        for (Bullet bullet : BulletContainer.getInstance().getBullets())
        {
            renderBullet(tank, bullet);
        }
    }

    public void renderBullet(Tank tank, Bullet bullet)
    {
        Image imgBullet = bullet.getImage();
        float xOffset = tank.getCamera().getOffset().getX();
        float yOffset = tank.getCamera().getOffset().getY();

        int padding = 15;

        if (bullet.isInitialState())
        {
            int heightDiff = tank.getHeight() - tank.getTankBase().getHeight();
            int widthDiff = tank.getWidth() - tank.getTankBase().getWidth();

            // Get Spawn points.
            float xSpawn = tank.getX() - widthDiff - xOffset;
            float ySpawn = tank.getY() - heightDiff - yOffset;

            // Set the location of the bullet
            gameManager.updateBulletLocation(bullet, new Point(xSpawn, ySpawn));
            gameManager.updateBulletInitialState(bullet,false);
        }

        imgBullet.setRotation(bullet.getRotation());
        imgBullet.setCenterOfRotation(imgBullet.getWidth()/2, imgBullet.getHeight() + padding);

        imgBullet.draw(bullet.getX() - bullet.getWidth()/2, bullet.getY() - bullet.getHeight() - padding);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException
    {
        tankManager.moveTank(tank);
        tankManager.shoot(tank);

        for (Bullet bullet : BulletContainer.getInstance().getBullets())
        {
            gameManager.move(bullet);
            gameManager.checkBulletConstraints(bullet, garbage);
        }

        disposeBullets();
    }

    public void disposeBullets()
    {
        for (Bullet bullet : garbage)
        {
            bullets.remove(bullet);
        }

        garbage.clear();
    }

    @Override
    public void keyPressed(int key, char c)
    {
        switch (key)
        {
            case Key.UP:
                tankManager.isDrivingForwards(tank, true);
                break;
            case Key.RIGHT:
                tankManager.rotateRight(tank);
                break;
            case Key.DOWN:
                tankManager.isDrivingBackwards(tank, true);
                break;
            case Key.LEFT:
                tankManager.rotateLeft(tank);
                break;
            case Key.SPACE:
                tankManager.shoot(tank);
                tankManager.shooting(tank,true);
                break;
        }

    }

    @Override
    public void keyReleased(int key, char c)
    {
        switch (key)
        {
            case Key.UP:
                tankManager.stop(tank);
                break;
            case Key.RIGHT:
                tankManager.stopTurningRight(tank);
                break;
            case Key.DOWN:
                tankManager.stop(tank);
                break;
            case Key.LEFT:
                tankManager.stopTurningLeft(tank);
                break;
            case Key.SPACE:
                tankManager.shooting(tank,false);
        }
    }
}
