package manager;

import engine.Engine;
import model.*;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.tiled.TiledMap;
import states.Game;

public class TankManager implements AssetManager
{

    /**
     * Constrains the tank, gives it boundaries to operate within.
     * @param tank          Tank to constrian
     * @param constraint    The constraint itself, which are just 4 x, y coordinates.
     */
    public void constrain(Tank tank, Constraint constraint)
    {
        tank.setConstraint(constraint);
    }

    /**
     * Attaches a camera to the tank. For now the Tanks can only have one.
     * @param tank      Tank to give camera.
     * @param camera    Camera to attach.
     */
    public void attachCamera(Tank tank, Camera camera)
    {
        tank.setCamera(camera);
    }

    @Override
    public Image findAsset(String name)
    {
        return TankContainer.getInstance().findAsset(name);
    }

    public void loadInAssets(String location, boolean clearAssets)
    {
        TankContainer.getInstance().loadInAssets(location, clearAssets);
    }

    /**
     * Shoots from the tank.
     *
     * @param tank Tank that's shooting.
     */
    public void shoot(Tank tank, int delta)
    {

        if ((System.currentTimeMillis() - tank.getLastFired()) >= tank.getRateOfFire() && tank.isShooting())
        {
            tank.setLastFired(System.currentTimeMillis());

            float spawnX = tank.getX() - (tank.getWidth());
            float spawnY = tank.getY() - (tank.getHeight() / 2);
            TiledMap map = MapContainer.getInstance().getMap();
            int mapWidthInPixels = map.getWidth() * map.getTileWidth();
            int mapHeightInPixels = map.getWidth() * map.getTileHeight();
            Constraint constraint = new Constraint(0, 0, mapWidthInPixels, mapHeightInPixels);
            Bullet bullet = new Bullet(tank, tank.getBullet(),
                    spawnX,
                    spawnY,
                    tank.getRotation(), constraint, null);
            // Use sin and cos to orient the bullet int the right direction
            float speed = bullet.getSpeed();
            float rotation = (float)Math.toRadians(bullet.getRotation());
            float xSpeed = ((float)Math.sin(rotation) * delta) * speed;
            float ySpeed = ((float)-Math.cos(rotation) * delta) * speed;
            bullet.setDx(xSpeed);
            bullet.setDy(ySpeed);

            BulletContainer.getInstance().addBullet(bullet);
        }
    }

    public void isDrivingForwards(Tank tank, boolean state)
    {
        tank.setIsDrivingBackwards(false);
        tank.setIsDrivingForwards(state);
    }

    public void isDrivingBackwards(Tank tank, boolean state)
    {
        tank.setIsDrivingForwards(false);
        tank.setIsDrivingBackwards(state);
    }

    /**
     * Uses trigonometry to calculate the correct direction when facing forwards.
     *
     * @param tank Player driving forward
     */
    public void driveForward(Tank tank, int delta)
    {
        tank.setIsDrivingForwards(true);
        float speed = tank.getSpeed();
        float rotation = (float)Math.toRadians(tank.getRotation());
        float xSpeed = ((float)Math.sin(rotation)*delta) * speed;
//        System.out.println("X speed: " + xSpeed);
        float ySpeed = ((float)-Math.cos(rotation)*delta) * speed;
//        System.out.println("Angle: " + player.getBufferRotation());
//        System.out.println("Y speed: " + ySpeed);
        tank.setDx(xSpeed);
        tank.setDy(ySpeed);
        GameManager.checkConstraints(tank, tank.getWidth(), tank.getHeight());
    }

    public void driveBackward(Tank tank, int delta)
    {
        tank.setIsDrivingBackwards(true);
        float speed = tank.getSpeed();
        float rotation = (float)Math.toRadians(tank.getRotation());
        float xSpeed = ((float)-Math.sin(rotation)*delta) * speed;
        float ySpeed = ((float)Math.cos(rotation)*delta) * speed;
        tank.setDx(xSpeed);
        tank.setDy(ySpeed);
        GameManager.checkConstraints(tank, tank.getWidth(), tank.getHeight());
    }

    /**
     * Specific logic for moving a tank.
     *
     * @param tank Tank to move.
     */
    public void moveTank(Tank tank, int delta)
    {
        if (!tank.isShooting())
        {
            if (tank.isDrivingForwards())
                driveForward(tank, delta);
            else if (tank.isDrivingBackwards())
                driveBackward(tank, delta);
            tank.setRotation(tank.getRotation() + tank.getDeltaRotation());
            GameManager.move(tank);

//            Point cameraPos = tank.getCamera().getOffset();
            // Set new camera position
            tank.getCamera().setXY(new Point(tank.getX() - (Engine.WIDTH / 2),
                    tank.getY() - (Engine.HEIGHT  / 2)));
        }
    }

    public void rotateRight(Tank tank)
    {
        tank.setDeltaRotation(GameRules.ROTATE_SPEED.getValue());
    }

    public void rotateLeft(Tank tank)
    {
        tank.setDeltaRotation(-GameRules.ROTATE_SPEED.getValue());
    }

    public void stop(Tank tank)
    {
        tank.setIsDrivingForwards(false);
        tank.setDy(0);
        tank.setDx(0);
    }

    public void stopTurningRight(Tank tank)
    {
        tank.setDx(0);
        if (tank.getDeltaRotation() != -GameRules.ROTATE_SPEED.getValue())
            tank.setDeltaRotation(0);
    }

    public void stopTurningLeft(Tank tank)
    {
        tank.setDx(0);
        if (tank.getDeltaRotation() != GameRules.ROTATE_SPEED.getValue())
            tank.setDeltaRotation(0);
    }

    public void shooting(Tank tank, boolean shooting)
    {
        tank.shooting(shooting);
    }
}
