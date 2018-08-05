package model;


import engine.Display;
import engine.Engine;
import org.newdawn.slick.geom.Vector2f;
import util.Box;

import javax.swing.*;

public class Camera extends Box
{

    public Camera(Sprite sprite)
    {
        super(sprite.getX(), sprite.getY(), Display.WIDTH, Display.HEIGHT);
        centerOn(sprite);
    }
    public Camera(float x, float y, float width, float height)
    {
        super(x, y, width, height);
    }

    public void centerOn(Sprite sprite)
    {
        width = Display.WIDTH / Display.SCALE_FACTORS[Display.scale];
        height = Display.HEIGHT / Display.SCALE_FACTORS[Display.scale];
        x = sprite.getX() - (width / 2);
        y = sprite.getY() - (height / 2);
    }
}
