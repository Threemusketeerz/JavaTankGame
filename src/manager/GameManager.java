package manager;

import model.GameRules;
import model.Tank;

import java.awt.event.KeyEvent;

public class GameManager
{

    /**
     * Uses trigonometry to calculate the correct direction when facing forwards.
     *
     * @param player Player driving forward
     */
    public void driveForward(Tank player)
    {
        double speed = GameRules.SPEED.getValue();
        double rotation = Math.toRadians(player.getBufferRotation());
        double xSpeed = Math.sin(rotation) * speed;
//        System.out.println("X speed: " + xSpeed);
        double ySpeed = -Math.cos(rotation) * speed;
//        System.out.println("Angle: " + player.getBufferRotation());
//        System.out.println("Y speed: " + ySpeed);
        player.setDx(xSpeed);
        player.setDy(ySpeed);
        checkConstraints(player);
    }

    public void move(Tank player)
    {
//        y += dy;
        if (player.isDriving())
            driveForward(player);
        player.setBufferRotation(player.getBufferRotation() + player.getDeltaRotation());
        player.setX(player.getX() + player.getDx());
        player.setY(player.getY() + player.getDy());
    }

    /**
     * Checks whether player is violating the constraints. It then "recalibrates the location of the player"
     * @param player    Player to check for
     */
    public void checkConstraints(Tank player)
    {
        // Fetch relevant data
        double minX = player.getConstraint().getMinX() + player.getWidth()/2;
        double minY = player.getConstraint().getMinY() + player.getHeight()/2;
        double maxX = player.getConstraint().getMaxX() - player.getWidth()/2;
        double maxY = player.getConstraint().getMaxY() - player.getHeight()/2;
        double playerX = player.getX();
        double playerY = player.getY();

        // TODO: This needs some love, you can exit out of the frame in the edges of the map.
        if (playerX <= minX)
        {
            player.setX(minX);
        }
        if (playerX >= maxX)
        {
            player.setX(maxX);
        }
        if (playerY <= minY)
        {
            player.setY(minY);
        }
        if (playerY >= maxY)
        {
            player.setY(maxY);
        }
    }


    // TODO: Reevaluate whether to keep key presses in Controller layer. We might want his this somewhere else.
    public void keyPressed(Tank player, KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (key)
        {
            case KeyEvent.VK_UP:
                //driveForward(player);
                player.setIsDriving(true);
                break;
            case KeyEvent.VK_RIGHT:
                player.setDeltaRotation(GameRules.ROTATE_SPEED.getValue());
                break;
//            case KeyEvent.VK_DOWN:
////                driveBackward(player);
//                break;
            case KeyEvent.VK_LEFT:
                player.setDeltaRotation(-GameRules.ROTATE_SPEED.getValue());
                break;
        }
    }

    public void keyReleased(Tank player, KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (key)
        {
            case KeyEvent.VK_UP:
//                player.setDy(0);
                player.setIsDriving(false);
                player.setDy(0);
                player.setDx(0);
                break;
            case KeyEvent.VK_RIGHT:
                player.setDx(0);
                if (player.getDeltaRotation() != -GameRules.ROTATE_SPEED.getValue())
                    player.setDeltaRotation(0);
                break;
            case KeyEvent.VK_DOWN:
                player.setDy(0);
                break;
            case KeyEvent.VK_LEFT:
                player.setDx(0);
                if (player.getDeltaRotation() != GameRules.ROTATE_SPEED.getValue())
                    player.setDeltaRotation(0);
                break;
        }
    }

}
