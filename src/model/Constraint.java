package model;

public class Constraint
{
    private double minX, minY, maxX, maxY;

    public Constraint(double minX, double minY, double maxX, double maxY)
    {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public double getMinX() { return minX; }
    public double getMinY() { return minY; }
    public double getMaxX() { return maxX; }
    public double getMaxY() { return maxY; }
}
