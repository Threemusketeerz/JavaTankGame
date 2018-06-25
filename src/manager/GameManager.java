package manager;

import model.*;

import java.awt.event.KeyEvent;

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

    public void shoot(Bullet bullet)
    {
        double speed = bullet.getSpeed();
        double rotation = Math.toRadians(bullet.getRotation());
        double xSpeed = Math.sin(rotation) * speed;
        double ySpeed = -Math.cos(rotation) * speed;

        bullet.setDx(xSpeed);
        bullet.setDy(ySpeed);
        checkConstraints(bullet, bullet.getWidth(), bullet.getHeight());
    }

    public void moveTank(Tank tank)
    {
        if (tank.isDriving())
            driveForward(tank);
        tank.setBufferRotation(tank.getRotation() + tank.getDeltaRotation());
        move(tank);
    }

    public void move(Sprite sprite)
    {
        sprite.setX(sprite.getX() + sprite.getDx());
        sprite.setY(sprite.getY() + sprite.getDy());
    }

    public void shoot(Tank tank)
    {
        double spawnX = tank.getX();
        double spawnY = tank.getY() - (tank.getHeight() / 2);
        Map map = MapContainer.getInstance().getMap();
        Constraint constraint = new Constraint(0, 0, map.getMap().getWidth(), map.getMap().getHeight());
        Bullet bullet = new Bullet(tank, tank.getBulletType(), spawnX, spawnY, tank.getRotation(), constraint, null);

        BulletContainer.getInstance().add(bullet);
    }

    /**
     * Checks whether player is violating the constraints. It then "recalibrates the location of the player"
     * @param sprite Player to check for
     */
    public void checkConstraints(Sprite sprite, int spriteWidth, int spriteHeight)
    {
        // Fetch relevant data
        if (sprite.getConstraint() == null)
            System.out.println("Sprite constraint not inititalized?");
        else
        {
            double minX = sprite.getConstraint().getMinX() + spriteWidth / 2;
            double minY = sprite.getConstraint().getMinY() + spriteHeight / 2;
            double maxX = sprite.getConstraint().getMaxX() - spriteWidth / 2;
            double maxY = sprite.getConstraint().getMaxY() - spriteHeight / 2;
            double spriteX = sprite.getX();
            double spriteY = sprite.getY();

            if (spriteX <= minX)
            {
                sprite.setX(minX);
            } else if (spriteX >= maxX)
            {
                sprite.setX(maxX);
            }

            if (spriteY <= minY)
            {
                sprite.setY(minY);
            } else if (spriteY >= maxY)
            {
                sprite.setY(maxY);
            }
        }
    }


    // TODO: Reevaluate whether to keep key presses in Controller layer. We might want his this somewhere else.
    public void keyPressed(Tank tank, KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (key)
        {
            case KeyEvent.VK_UP:
                //driveForward(tank);
                tank.setIsDriving(true);
                break;
            case KeyEvent.VK_RIGHT:
                tank.setDeltaRotation(GameRules.ROTATE_SPEED.getValue());
                break;
//            case KeyEvent.VK_DOWN:
////                driveBackward(tank);
//                break;
            case KeyEvent.VK_LEFT:
                tank.setDeltaRotation(-GameRules.ROTATE_SPEED.getValue());
                break;
            case KeyEvent.VK_SPACE:
                shoot(tank);
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
                tank.setIsDriving(false);
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
        }
    }

}
