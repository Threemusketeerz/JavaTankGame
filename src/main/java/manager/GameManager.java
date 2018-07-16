package manager;

import model.*;
import util.Point;

import java.util.ArrayList;

public class GameManager
{



    /**
     * Calculate new position
     *
     * @param sprite Sprite to move
     */
    public static void move(Sprite sprite)
    {
        sprite.setX(sprite.getX() + sprite.getDx());
        sprite.setY(sprite.getY() + sprite.getDy());
    }


    /**
     * Checks whether player is violating the constraints. It then "recalibrates the location of the player"
     *
     * @param sprite Player to check for
     */
    public static boolean checkConstraints(Sprite sprite, int spriteWidth, int spriteHeight, double xOffset, double yOffset)
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

    public static boolean checkConstraints(Sprite sprite, int spriteWidth, int spriteHeight)
    {
        return checkConstraints(sprite, spriteWidth, spriteHeight, 0, 0);
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
