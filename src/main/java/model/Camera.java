package model;


import org.newdawn.slick.geom.Vector2f;
import util.Box;

import javax.swing.*;

public class Camera extends Box
{
    public Camera(float x, float y, float width, float height)
    {
        super(x, y, width, height);
    }

    public void centerOn(Sprite sprite)
    {
        x = sprite.getX() - (width / 2);
        y = sprite.getY() - (height / 2);
    }
}
