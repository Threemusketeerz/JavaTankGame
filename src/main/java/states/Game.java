package states;


import engine.Engine;
import manager.BulletManager;
import manager.GameManager;
import manager.TankManager;
import model.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.effects.FireEmitter;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import util.Key;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game extends BasicGameState
{
    public static final int ID = 0;

    private static final float[] scaleFactors = {0.8f, 1f, 1.2f, 1.4f, 1.6f, 1.8f, 2f};

    private static int scale = 1;

    private TiledMap map;
    private Tank tank;
    private ArrayList<Bullet> bullets;
    private ArrayList<Bullet> garbage;
    private TankManager tankManager;
    private BulletManager bulletManager;
    private GameManager gameManager;

    private FireEmitter fireEmitter;


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
        map = new TiledMap("Maps/Map02.tmx");
        MapContainer.getInstance().setMap(map);

        int mapWidth = map.getWidth() * map.getTileWidth();
        int mapHeight = map.getHeight() * map.getTileHeight();

        tank = new Tank(tankManager.findAsset("blueTank01_big.png"),
                bulletManager.findAsset("bullet01_big.png"),
                "Player 1",
                mapWidth / 2, mapHeight / 2, 0f,
                new Constraint(0, 0, mapWidth, mapHeight),
                null
        );

        tankManager.attachCamera(tank,
                new Camera(tank.getX() - Engine.WIDTH / 2,
                        tank.getY() - Engine.HEIGHT / 2,
                        Engine.WIDTH, Engine.HEIGHT));

        fireEmitter = new FireEmitter();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException
    {

        Camera camera = tank.getCamera();

        calcCameraPosition();

        float xOffset = camera.getX();
        float yOffset = camera.getY();

        float xOffsetScaled = xOffset;
        float yOffsetScaled = yOffset;

        graphics.scale(scaleFactors[scale], scaleFactors[scale]);

//        int xStart = (int)Math.floor(xOffset/map.getTileWidth());
//        int yStart = (int)Math.floor(yOffset/map.getTileHeight());
//        int xEnd = (int)Math.floor(camera.getWidth() / map.getTileWidth());
//        int yEnd = (int)Math.floor(camera.getHeight() / map.getTileHeight());

        map.render((int) -xOffsetScaled, (int) -yOffsetScaled);
//        map.render(16, 16,
//                xStart, yStart,
//                xEnd, yEnd);

        // Render bullets first, so they appear to go through the tank.
        renderBullets(xOffset, yOffset);
        renderTank(xOffset, yOffset);


        graphics.drawString("xOffset:       " + xOffset, 90f, 0f);
        graphics.drawString("yOffset:       " + yOffset, 90f, 15f);
//        graphics.drawString("xOffsetScaled: " + xOffsetScaled, 90f, 30f);
//        graphics.drawString("yOffsetScaled: " + yOffsetScaled, 90f, 45f);
        graphics.drawString("xStart:        " + (int)xOffset/map.getTileWidth(), 90f, 30f);
        graphics.drawString("yStart:        " + (int)yOffset/map.getTileHeight(), 90f, 45f);

        // Fireemitter test
    }

    public void renderTank(float xOffset, float yOffset)
    {
        float xTank = tank.getX() - xOffset;
        float yTank = tank.getY() - yOffset;

        Image tankBase = tank.getTankBase();

        // Draw base
        tankBase.setRotation(tank.getRotation());
        tankBase.draw(xTank - (tankBase.getWidth()) / 2, yTank - (tankBase.getHeight()) / 2);
    }

    public void renderBullets(float xOffset, float yOffset)
    {
        for (Bullet bullet : BulletContainer.getInstance().getBullets())
        {
            renderBullet(tank, bullet, xOffset, yOffset);
        }
    }

    public void renderBullet(Tank tank, Bullet bullet, float xOffset, float yOffset)
    {
        Image imgBullet = bullet.getImage();

        int padding = 15;

        if (bullet.isInitialState())
        {
            float heightDiff = tank.getHeight() - tank.getTankBase().getHeight();
            float widthDiff = tank.getWidth() - tank.getTankBase().getWidth();

            // Get Spawn points.
            float xSpawn = tank.getX() - widthDiff - xOffset;
            float ySpawn = tank.getY() - heightDiff - yOffset;

            // Set the location of the bullet
            gameManager.updateBulletLocation(bullet, new Point(xSpawn, ySpawn));
            gameManager.updateBulletInitialState(bullet, false);
        }

        imgBullet.setRotation(bullet.getRotation());
        imgBullet.setCenterOfRotation(imgBullet.getWidth() / 2, imgBullet.getHeight() + padding);

        imgBullet.draw(bullet.getX() - bullet.getWidth() / 2, bullet.getY() - bullet.getHeight() - padding);
    }

    public void calcCameraPosition()
    {
        Camera camera = tank.getCamera();

        int mapWidth = map.getWidth() * map.getTileWidth();
        int mapHeight = map.getHeight() * map.getTileHeight();

        // Calculate camera position according to scale.
        camera.setWidth(Engine.WIDTH / scaleFactors[scale]);
        camera.setHeight(Engine.HEIGHT / scaleFactors[scale]);
        // Set the new X according to the width
        camera.centerOn(tank);

        if (camera.getEndX() >= mapWidth)
            camera.setX(mapWidth - camera.getWidth());
        else if (camera.getX() <= 0)
            camera.setX(0);

        if (camera.getEndY() >= mapHeight)
            camera.setY(mapHeight - camera.getHeight());
        else if (camera.getY() <= 0)
            camera.setY(0);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException
    {
        System.out.println(delta);
        tankManager.moveTank(tank, delta);
        tankManager.shoot(tank, delta);

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
//                tankManager.shoot(tank, delta);
                tankManager.shooting(tank, true);
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
                tankManager.shooting(tank, false);
        }
    }

    @Override
    public void mouseWheelMoved(int change)
    {
//        System.out.println("MouseWheel: " + change);
        if (change > 0 && scale < scaleFactors.length - 1)
        {
            scale += 1;
        }

        if (change < 0 && scale > 1)
        {
            scale -= 1;
        }
    }
}
