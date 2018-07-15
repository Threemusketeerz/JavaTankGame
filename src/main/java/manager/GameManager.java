package manager;

import model.*;
import util.Point;
import view.GameView;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameManager
{

    /**
     * Uses trigonometry to calculate the correct direction when facing forwards.
     *
     * @param tank Player driving forward
     */
    public void driveForward(Tank tank)
    {
        double speed = GameRules.SPEED.getValue();
        double rotation = Math.toRadians(tank.getRotation());
        double xSpeed = Math.sin(rotation) * speed;
//        System.out.println("X speed: " + xSpeed);
        double ySpeed = -Math.cos(rotation) * speed;
//        System.out.println("Angle: " + player.getBufferRotation());
//        System.out.println("Y speed: " + ySpeed);
        tank.setDx(xSpeed);
        tank.setDy(ySpeed);
        checkConstraints(tank, tank.getWidth(), tank.getHeight());
    }

    public void driveBackward(Tank tank)
    {
        double speed = GameRules.SPEED.getValue();
        double rotation = Math.toRadians(tank.getRotation());
        double xSpeed = -Math.sin(rotation) * speed;
        double ySpeed = Math.cos(rotation) * speed;
        tank.setDx(xSpeed);
        tank.setDy(ySpeed);
        checkConstraints(tank, tank.getWidth(), tank.getHeight());
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
            move(tank);

            Point cameraPos = tank.getCamera().getOffset();
            // Set new camera position
            tank.getCamera().setOffset(new Point(tank.getX() - GameView.WIDTH / 2,
                    tank.getY() - GameView.HEIGHT / 2));
        }
    }

    /**
     * Calculate new position
     *
     * @param sprite Sprite to move
     */
    public void move(Sprite sprite)
    {
        sprite.setX(sprite.getX() + sprite.getDx());
        sprite.setY(sprite.getY() + sprite.getDy());
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

    /**
     * Checks whether player is violating the constraints. It then "recalibrates the location of the player"
     *
     * @param sprite Player to check for
     */
    public boolean checkConstraints(Sprite sprite, int spriteWidth, int spriteHeight, double xOffset, double yOffset)
    {
        boolean violationState = false;
        // Fetch relevant data
        if (sprite.getConstraint() == null)
            System.out.println("Sprite constraint not inititalized?");
        else
        {
            double minX = sprite.getConstraint().getMinX() + spriteWidth / 2 - xOffset;
            double minY = sprite.getConstraint().getMinY() + spriteHeight / 2 - yOffset;
            double maxX = sprite.getConstraint().getMaxX() - spriteWidth / 2 - xOffset;
            double maxY = sprite.getConstraint().getMaxY() - spriteHeight / 2 - yOffset;
            double spriteX = sprite.getX();
            double spriteY = sprite.getY();

            if (spriteX <= minX)
            {
                violationState = true;
                sprite.setX(minX);
            } else if (spriteX >= maxX)
            {
                violationState = true;
                sprite.setX(maxX);
            }

            if (spriteY <= minY)
            {
                violationState = true;
                sprite.setY(minY);
            } else if (spriteY >= maxY)
            {
                violationState = true;
                sprite.setY(maxY);
            }
        }
        return violationState;
    }

    public void checkBulletConstraints(Bullet bullet, ArrayList<Bullet> garbage)
    {
        double xOffset = bullet.getTank().getCamera().getOffset().getX();
        double yOffset = bullet.getTank().getCamera().getOffset().getY();

        if (checkConstraints(bullet, bullet.getWidth(), bullet.getHeight(), xOffset, yOffset))
        {
//            System.out.println("----- Bullet violating ----- \n"
//                    + "Bullet id: " + bullet.getId()
//                    + "\nBulletX: " + bullet.getX()
//                    + "\nBulletY: " + bullet.getY());

            garbage.add(bullet);
        }
    }

    public boolean checkConstraints(Sprite sprite, int spriteWidth, int spriteHeight)
    {
        return checkConstraints(sprite, spriteWidth, spriteHeight, 0, 0);
    }

    // TODO: Reevaluate whether to keep key presses in Controller layer. We might want his this somewhere else.
    public void keyPressed(Tank tank, KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (key)
        {
            case KeyEvent.VK_UP:
                //driveForward(tank);
                tank.setIsDrivingForwards(true);
                break;
            case KeyEvent.VK_RIGHT:
                tank.setDeltaRotation(GameRules.ROTATE_SPEED.getValue());
                break;
            case KeyEvent.VK_DOWN:
                driveBackward(tank);
                tank.setIsDrivingBackwards(true);
                break;
            case KeyEvent.VK_LEFT:
                tank.setDeltaRotation(-GameRules.ROTATE_SPEED.getValue());
                break;
            case KeyEvent.VK_SPACE:
                shoot(tank);
                tank.shooting(true);
                break;
        }
    }

    public void keyReleased(Tank tank, KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (key)
        {
            case KeyEvent.VK_UP:
//                player.setDy(0);
                tank.setIsDrivingForwards(false);
                tank.setDy(0);
                tank.setDx(0);
                break;
            case KeyEvent.VK_RIGHT:
                tank.setDx(0);
                if (tank.getDeltaRotation() != -GameRules.ROTATE_SPEED.getValue())
                    tank.setDeltaRotation(0);
                break;
            case KeyEvent.VK_DOWN:
                tank.setDy(0);
                break;
            case KeyEvent.VK_LEFT:
                tank.setDx(0);
                if (tank.getDeltaRotation() != GameRules.ROTATE_SPEED.getValue())
                    tank.setDeltaRotation(0);
                break;
            case KeyEvent.VK_SPACE:
                tank.shooting(false);
        }
    }

    public void updateBulletInitialState(Bullet bullet, boolean state)
    {
        bullet.setInitialState(state);
    }

    public void updateBulletLocation(Bullet bullet, Point point)
    {
        bullet.setX(point.getX());
        bullet.setY(point.getY());
    }

}
