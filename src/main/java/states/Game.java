package states;


import engine.Display;
import manager.BulletManager;
import manager.GameManager;
import manager.TankManager;
import model.*;
import parser.tiled.io.TMXFile;
import parser.tiled.TiledMap;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import util.Key;

import java.util.ArrayList;
import java.util.Iterator;

public class Game extends BasicGameState
{
    public static final int         ID = 0;


    private TiledMap                map;
    private Tank                    tank;
    private ArrayList<Bullet>       bullets;
    private ArrayList<Explosion>    explosions;
    private TankManager             tankManager;
    private BulletManager           bulletManager;
    private GameManager             gameManager;
    private Camera                  camera;

    private ArrayList<ParticleSystem> particleSystems;

    @Override
    public int getID()
    {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException
    {
//        createParticleEmitterFile("explosion");

        // Setup instances
        tankManager = new TankManager();
        bulletManager = new BulletManager();
        gameManager = new GameManager();
        bullets = BulletContainer.getInstance().getBullets();
        explosions = new ArrayList<>();
        particleSystems = new ArrayList<>();
        TMXFile tmxFile = new TMXFile("Maps/Map01.tmx");
        map = tmxFile.generateTiledMap();
        MapContainer.getInstance().setMap(map);

        int mapWidth = map.getWidth() * map.getTileWidth();
        int mapHeight = map.getHeight() * map.getTileHeight();

        tank = new Tank(tankManager.findAsset("blueTank01_big.png"),
                bulletManager.findAsset("bullet01_big.png"),
                "Player 1",
//                mapWidth / 2, mapHeight / 2, 0f,
                Display.WIDTH/2 / Display.getScale(), Display.HEIGHT/2 / Display.getScale(), 0f,
                new Constraint(0, 0, mapWidth, mapHeight),
                null
        );

        camera = new Camera(tank);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException
    {
        calcCameraPosition(tank);

        float xOffset = camera.getX();
        float yOffset = camera.getY();

        graphics.scale(Display.SCALE_FACTORS[Display.scale], Display.SCALE_FACTORS[Display.scale]);


        map.render((int) -xOffset, (int) -yOffset);

        // Render bullets first, so they appear to go through the tank.
        renderBullets(xOffset, yOffset);
        renderTank(xOffset, yOffset);
        renderExplosions(xOffset, yOffset, graphics);


        graphics.drawString("xOffset:       " + xOffset, 90f, 0f);
        graphics.drawString("yOffset:       " + yOffset, 90f, 15f);
        graphics.drawString("xStart:        " + (int)xOffset/map.getTileWidth(), 90f, 30f);
        graphics.drawString("yStart:        " + (int)yOffset/map.getTileHeight(), 90f, 45f);

//        disposeBullets(new Vector2f(xOffset, yOffset));

    }

    public void updateExplosions(int delta)
    {
        ArrayList<Explosion> garbage = new ArrayList<>();
        Iterator<Explosion> it = explosions.iterator();
        while(it.hasNext())
        {
            Explosion explosion = it.next();
            if (explosion.isFinishedExpanding())
            {
                garbage.add(explosion);
                it.remove();
            }
            else
                explosion.expand(delta);
        }

        explosions.removeAll(garbage);
    }
    public void renderExplosions(float xOffset, float yOffset, Graphics graphics)
    {
        for (Explosion explosion : explosions)
        {
            graphics.setColor(new Color(255, 0, 0, 255));
            // draw our own oval to count for camera offset.
//            graphics.draw(explosion);
            graphics.fillOval(explosion.getCenterX() - xOffset,
                    explosion.getCenterY() - yOffset,
                    explosion.getRadius(), explosion.getRadius());
        }
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
            float xSpawn = tank.getX() - widthDiff;
            float ySpawn = tank.getY() - heightDiff;

            // Set the location of the bullet
            gameManager.updateBulletLocation(bullet, new Point(xSpawn, ySpawn));
            gameManager.updateBulletInitialState(bullet, false);
        }

        imgBullet.setRotation(bullet.getRotation());
        imgBullet.setCenterOfRotation(imgBullet.getWidth() / 2, imgBullet.getHeight() + padding);

        imgBullet.draw(bullet.getX() - (bullet.getWidth() / 2) - xOffset - tank.getDx(),
                bullet.getY() - bullet.getHeight() - padding - yOffset + tank.getDy());
    }

    public void calcCameraPosition(Sprite sprite)
    {

        int mapWidth = map.getWidth() * map.getTileWidth();
        int mapHeight = map.getHeight() * map.getTileHeight();

        camera.centerOn(sprite);

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
        tankManager.moveTank(tank, delta);
        tankManager.shoot(tank);

        updateExplosions(delta);

        updateBullets(delta);

    }

    public void updateBullets(int delta)
    {
        ArrayList<Bullet> garbage = new ArrayList<>();
        for (Bullet bullet : bullets)
        {
            gameManager.move(bullet, delta);
            gameManager.checkBulletConstraints(bullet);
            if(bullet.hasExploded())
            {
                explode(new Vector2f(bullet.getCenterX(), bullet.getCenterY()));
                garbage.add(bullet);
            }
        }

        bullets.removeAll(garbage);

    }

//    public void disposeBullets(Vector2f pos)
//    {
//        Iterator<Bullet> it = bullets.iterator();
//        while(it.hasNext())
//        {
//            Bullet bullet = it.next();
//
//            System.out.println("BulletX:        " + bullet.getX());
//            System.out.println("BulletY:        " + bullet.getY());
//            System.out.println("BulletX Offset  " + (bullet.getX() - pos.x));
//            System.out.println("BulletY Offset: " + (bullet.getY() - pos.y));
//            if (bullet.hasExploded())
//            {
//                explode(new Vector2f(bullet.getX(), bullet.getY()));
//                bullets.remove(bullet);
//            }
//        }
//    }

    public void explode(Vector2f pos)
    {
        Explosion explosion = new Explosion(pos.x, pos.y);
        explosions.add(explosion);
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
        if (change > 0 && Display.scale < Display.SCALE_FACTORS.length - 1)
        {
            Display.scale += 1;
            camera.setWidth(Display.WIDTH / Display.getScale());
        }

        if (change < 0 && Display.scale > 1)
        {
            Display.scale -= 1;
            camera.setHeight(Display.HEIGHT / Display.getScale());
        }
    }

}
