package manager;

import model.GameRules;
import model.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class GameManager
{
    public void rotateSprite(Sprite sprite, double degrees)
    {
//        System.out.println("Rotating");
        Graphics2D gc = (Graphics2D) sprite.getLoadedImage().getGraphics();
        AffineTransform affineTransform = gc.getTransform();
        AffineTransform newTransform = new AffineTransform();
        newTransform.translate(sprite.getX(), sprite.getY());
        affineTransform.rotate(Math.toRadians(degrees), sprite.getLoadedImage().getWidth() / 2, sprite.getLoadedImage().getHeight() / 2);
        affineTransform.translate(sprite.getX(), sprite.getY());
    }

    public void keyPressed(Sprite player, KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (key)
        {
            case KeyEvent.VK_UP:
                player.setDx(-GameRules.SPEED.getValue());
                break;
            case KeyEvent.VK_RIGHT:
                rotateSprite(player, GameRules.ROTATE_SPEED.getValue());
//                player.setDx(GameRules.SPEED.getValue());
                break;
            case KeyEvent.VK_DOWN:
                player.setDy(GameRules.SPEED.getValue());
                break;
            case KeyEvent.VK_LEFT:
                rotateSprite(player, GameRules.ROTATE_SPEED.getValue());
//                player.setDx(-GameRules.SPEED.getValue());
                break;
        }
    }

    public void keyReleased(Sprite player, KeyEvent e)
    {
        int key = e.getKeyCode();

        switch (key)
        {
            case KeyEvent.VK_UP:
                player.setDy(0);
                break;
            case KeyEvent.VK_RIGHT:
                player.setDx(0);
                break;
            case KeyEvent.VK_DOWN:
                player.setDy(0);
                break;
            case KeyEvent.VK_LEFT:
                player.setDx(0);
                break;
        }
    }

    public void move(Sprite player)
    {
        player.move();
    }
}
