package manager;

import model.*;
import util.Point;
import view.Game;

import java.awt.image.BufferedImage;

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
    public BufferedImage findAsset(String name)
    {
        return TankContainer.getInstance().findAsset(name);
    }

    /**
     * Shoots from the tank.
     *
     * @param tank Tank that's shooting.
     */
    public void shoot(Tank tank)
    {

        if ((System.currentTimeMillis() - tank.getLastFired()) >= tank.getRateOfFire() && tank.isShooting())
        {
            tank.setLastFired(System.currentTimeMillis());

            double spawnX = tank.getX() - (tank.getWidth());
            double spawnY = tank.getY() - (tank.getHeight() / 2);
            TankMap map = MapContainer.getInstance().getMap();
            Constraint constraint = new Constraint(0, 0, map.getWidthInPixels(), map.getHeightInPixels());
            Bullet bullet = new Bullet(tank, tank.getBullet(),
                    spawnX,
                    spawnY,
                    tank.getRotation(), constraint, null);
            // Use sin and cos to orient the bullet int the right direction
            double speed = bullet.getSpeed();
            double rotation = Math.toRadians(bullet.getRotation());
            double xSpeed = Math.sin(rotation) * speed;
            double ySpeed = -Math.cos(rotation) * speed;
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
    public void driveForward(Tank tank)
    {
        tank.setIsDrivingForwards(true);
        double speed = GameRules.SPEED.getValue();
        double rotation = Math.toRadians(tank.getRotation());
        double xSpeed = Math.sin(rotation) * speed;
//        System.out.println("X speed: " + xSpeed);
        double ySpeed = -Math.cos(rotation) * speed;
//        System.out.println("Angle: " + player.getBufferRotation());
//        System.out.println("Y speed: " + ySpeed);
        tank.setDx(xSpeed);
        tank.setDy(ySpeed);
        GameManager.checkConstraints(tank, tank.getWidth(), tank.getHeight());
    }

    public void driveBackward(Tank tank)
    {
        tank.setIsDrivingBackwards(true);
        double speed = GameRules.SPEED.getValue();
        double rotation = Math.toRadians(tank.getRotation());
        double xSpeed = -Math.sin(rotation) * speed;
        double ySpeed = Math.cos(rotation) * speed;
        tank.setDx(xSpeed);
        tank.setDy(ySpeed);
        GameManager.checkConstraints(tank, tank.getWidth(), tank.getHeight());
    }

    /**
     * Specific logic for moving a tank.
     *
     * @param tank Tank to move.
     */
    public void moveTank(Tank tank)
    {
        if (!tank.isShooting())
        {
            if (tank.isDrivingForwards())
                driveForward(tank);
            else if (tank.isDrivingBackwards())
                driveBackward(tank);
            tank.setBufferRotation(tank.getRotation() + tank.getDeltaRotation());
            GameManager.move(tank);

//            Point cameraPos = tank.getCamera().getOffset();
            // Set new camera position
            tank.getCamera().setOffset(new Point(tank.getX() - Game.WIDTH / 2,
                    tank.getY() - Game.HEIGHT / 2));
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
