package context;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;

public class Sprite
{
    private int x, y, deltaX, deltaY, speed;
    private final int width = 200, height = 200;
    private BufferedImage image;
    private View view;
    private Board board;

    public Sprite(Board board)
    {
        this.x = board.getW() / 2;
        this.y = board.getY() / 2;
        view = new View(x, y, board.getW(), board.getH(),
                200,200, board);
    }

    public Sprite(int x, int y, int speed, Board board)
    {
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.board = board;
//        View = new View(x, y, board.getW(), board.getH());
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public void moveX(int speed) { this.x += speed; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public void moveY(int speed) { this.y += speed; }
//    public Image getImage() { return image; }
//    public Board getBoard() { return board; }
    public View getView() { return view; }

    public void update()
    {
        move();
        view.update(x, y);
    }

    public void move()
    {
        if(x > board.getW())
            x = board.getW();
        else if(x < 0)
            x = 0;
        else
            x += deltaX;

        if(y > board.getH())
            y = board.getH();
        else if (y < 0)
            y = 0;
        else
            y += deltaY;
    }

    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();

        switch(key)
        {
            case KeyEvent.VK_LEFT:
                deltaX = speed;
                break;
            case KeyEvent.VK_RIGHT:
                deltaX = -speed;
                break;
            case KeyEvent.VK_DOWN:
                deltaY = speed;
                break;
            case KeyEvent.VK_UP:
                y = -speed;
                break;
        }
    }

    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();

        switch(key)
        {
            case KeyEvent.VK_LEFT:
                deltaX = 0;
                break;
            case KeyEvent.VK_RIGHT:
                deltaX = 0;
                break;
            case KeyEvent.VK_DOWN:
                deltaY = 0;
                break;
            case KeyEvent.VK_UP:
                deltaY = 0;
                break;
        }
    }

}
