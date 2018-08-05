package manager;

import model.*;
import org.newdawn.slick.geom.Point;

import java.util.ArrayList;

public class GameManager
{
    /**
     * Calculate new position
     *
     * @param sprite Sprite to move
     */
    public static void move(Sprite sprite, int delta)
    {
        sprite.setX(sprite.getX() + (sprite.getDx() * delta));
        sprite.setY(sprite.getY() + (sprite.getDy() * delta));
    }


    /**
     * Checks whether player is violating the constraints. It then "recalibrates the location of the player"
     *
     * @param sprite Player to check for
     */
    public static boolean checkConstraints(Sprite sprite, int spriteWidth, int spriteHeight, float xOffset, float yOffset)
    {
        boolean violationState = false;
        // Fetch relevant data
        if (sprite.getConstraint() == null)
            System.out.println("Sprite constraint not inititalized?");
        else
        {
            float minX = sprite.getConstraint().getX() + spriteWidth / 2 - xOffset;
            float minY = sprite.getConstraint().getY() + spriteHeight / 2 - yOffset;
            float maxX = sprite.getConstraint().getEndX() - spriteWidth / 2 - xOffset;
            float maxY = sprite.getConstraint().getEndY() - spriteHeight / 2 - yOffset;
            float spriteX = sprite.getX();
            float spriteY = sprite.getY();

            if (spriteX <= minX)
            {
                violationState = true;
                sprite.setX(minX);
            }
            else if (spriteX >= maxX)
            {
                violationState = true;
                sprite.setX(maxX);
            }

            if (spriteY <= minY)
            {
                violationState = true;
                sprite.setY(minY);
            }
            else if (spriteY >= maxY)
            {
                violationState = true;
                sprite.setY(maxY);
            }
        }
        return violationState;
    }

    public static void checkBulletConstraints(Bullet bullet)
    {
        long livedTime = System.currentTimeMillis() - bullet.getSpawnTime();
        if (livedTime > bullet.LIFE_TIME)
        {
            bullet.explode();
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
