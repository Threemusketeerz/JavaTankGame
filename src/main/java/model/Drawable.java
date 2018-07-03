package model;

import java.awt.image.BufferedImage;

public interface Drawable
{
    double getX();
    double getY();
    double getRotation();
    int getWidth();
    int getHeight();
    BufferedImage getImage();
}
