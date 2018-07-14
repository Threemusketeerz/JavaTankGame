package model;

import util.Point;

public class Camera
{
    private Point offset;

    public Camera(Point offset)
    {
        this.offset = offset;
    }

    public Point getOffset() { return offset; }

    public void setOffset(Point offset) { this.offset = offset; }
}
