package util;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class Box extends Rectangle
{
    public Box(float x, float y, float width, float height)
    {
        super(x, y, width, height);
    }

    public float getCenterX() { return x + (width/2); }
    public float getCenterY() { return y + (height/2); }

    public float getEndX() { return x + width; }
    public float getEndY() { return y + height; }

    public void setXY(Point point)
    {
        this.x = point.getX();
        this.y = point.getY();
    }
}
