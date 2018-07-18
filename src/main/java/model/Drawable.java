package model;

import org.newdawn.slick.Image;

import java.awt.image.BufferedImage;

public interface Drawable
{
    float getX();
    float getY();
    float getRotation();
    int getWidth();
    int getHeight();
    Image getImage();
}
