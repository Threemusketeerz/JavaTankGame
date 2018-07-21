package model;

import util.Box;

public class Constraint extends Box
{
    public Constraint(float x, float y, float width, float height)
    {
        super(x, y, width, height);
    }

    /**
     * If x and y are within the bounds of Constraint return true, else false
     * @param x
     * @param y
     * @return
     */
    public boolean withinConstraint(float x, float y)
    {
        return  x < getX() || y < getY()  || x > getEndX() || y > getEndY() ? false : true;
    }

    /**
     * reverse of withinConstraint
     * @param x     x to check for
     * @param y     y to check for
     * @return
     */
    public boolean outsideConstrain(float x, float y)
    {
        return !withinConstraint(x, y);
    }
}
