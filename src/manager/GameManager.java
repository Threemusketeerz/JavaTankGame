package manager;

import model.GameRules;
import model.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GameManager
{

    public void driveForward(Sprite player)
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
    }

    public void move(Sprite player)
    {
//        y += dy;
        if (player.isDriving())
            driveForward(player);
        player.setBufferRotation(player.getBufferRotation() + player.getDeltaRotation());
        player.setX(player.getX() + player.getDx());
        player.setY(player.getY() + player.getDy());
    }

    // TODO: Reevaluate whether to keep key presses in Controller layer. We might want his this somewhere else.
    public void keyPressed(Sprite player, KeyEvent e)
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

    public void keyReleased(Sprite player, KeyEvent e)
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

//    public void move(Sprite player)
//    {
//        player.move();
//    }
}
