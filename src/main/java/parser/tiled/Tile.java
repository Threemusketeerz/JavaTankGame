package parser.tiled;

import org.newdawn.slick.Image;

public class Tile
{
    private int gid;
    private Image image;

    public Tile(int gid)
    {
        this.gid = gid;
    }

    public Image getImage()
    {
        return image;
    }

    public void setImage(Image image)
    {
        this.image = image;
    }

    /**
     * Renders the image to position by which the image finds itself
     * @param width     Width of layer
     * @param height    Height of layer
     */
    public void render()
    {
        image.draw();
}

    public int getX(int width)
    {
        return (gid - 1) % width;
    }

    public int getY(int width)
    {
        return gid / width;
    }

    public int getGid()
    {
        return gid;
    }
}
