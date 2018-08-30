package states;

import engine.Display;
import manager.BulletManager;
import manager.GameManager;
import manager.TankManager;
import org.newdawn.slick.geom.Rectangle;
//import parser.tiled.io.TMXFile;
//import parser.tiled.TiledMap;
import model.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import util.Key;

import java.util.ArrayList;
import java.util.Iterator;

public class GameNoCamera extends BasicGameState
{

    public static final int         ID = 0;


//    private TiledMap                map;
    private Tank                    tank;
    private ArrayList<Bullet>       bullets;
    private ArrayList<Explosion>    explosions;
    private TankManager             tankManager;
    private BulletManager           bulletManager;
    private GameManager             gameManager;

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
        tankManager     = new TankManager();
        bulletManager   = new BulletManager();
        gameManager     = new GameManager();
        bullets         = BulletContainer.getInstance().getBullets();
        explosions      = new ArrayList<>();
//        TMXFile tmxFile = new TMXFile("Maps/TestMapFinal.tmx");
//        map             = tmxFile.generateTiledMap();
//        MapContainer.getInstance().setMap(map);

//        int mapWidth = map.getWidth() * map.getTileWidth();
//        int mapHeight = map.getHeight() * map.getTileHeight();

        tank = new Tank(tankManager.findAsset("blueTank01_big.png"),
                bulletManager.findAsset("bullet01_big.png"),
                "Player 1",
//                mapWidth / 2, mapHeight / 2, 0f,
                Display.WIDTH/2 / Display.getScale(), Display.HEIGHT/2 / Display.getScale(), 0f,
//                new Constraint(0, 0, mapWidth, mapHeight),
                null,
                null
        );

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException
    {
        graphics.scale(Display.SCALE_FACTORS[Display.scale], Display.SCALE_FACTORS[Display.scale]);

//        map.render(0, 0);
//
        // Render bullets first, so they appear to go through the tank.
        renderBullets();
        renderTank();
        renderExplosions(graphics);

        graphics.setColor(Color.red);
        graphics.fill(new Rectangle(0, 0, 100, 100));

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
                garbage.add(explosion);
            else
                explosion.expand(delta);
        }

        explosions.removeAll(garbage);
    }
    public void renderExplosions(Graphics graphics)
    {
        for (Explosion explosion : explosions)
        {
            graphics.setColor(new Color(255, 0, 0, 255));
            // draw our own oval to count for camera offset.
//            graphics.draw(explosion);
            graphics.fillOval(explosion.getCenterX(),
                    explosion.getCenterY(),
                    explosion.getRadius(), explosion.getRadius());
        }
    }

    public void renderTank()
    {
        float xTank = tank.getX();
        float yTank = tank.getY();

        Image tankBase = tank.getTankBase();

        // Draw base
        tankBase.setRotation(tank.getRotation());
        tankBase.draw(xTank - (tankBase.getWidth()) / 2, yTank - (tankBase.getHeight()) / 2);
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

        imgBullet.draw(bullet.getX() - (bullet.getWidth() / 2) - tank.getDx(),
                bullet.getY() - bullet.getHeight() - padding + tank.getDy());
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

    private void explode(Vector2f pos)
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
        }

        if (change < 0 && Display.scale > 1)
        {
            Display.scale -= 1;
        }
    }
}
