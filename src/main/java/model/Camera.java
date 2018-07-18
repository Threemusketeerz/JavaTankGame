package model;


import org.newdawn.slick.geom.Point;

public class Camera
{
    private Point offset;

    /**
     * @param offset Wished offset, usually screenWidth/2 & screenHeight/2
     * @param tank   Tank to observe
     */
    public Camera(Point offset)
    {
        this.offset = offset;
    }

    public Point    getOffset() { return offset; }

    public void     setOffset(Point offset) { this.offset = offset; }
}
