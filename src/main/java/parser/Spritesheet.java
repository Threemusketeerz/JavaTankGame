package parser;

import model.Sprite;

import java.awt.image.BufferedImage;

public class Spritesheet
{

    private String path;
    private BufferedImage spriteSheet;

    public Spritesheet(String path)
    {
        this.path = path;
        spriteSheet = Sprite.loadImage(path);
    }

    public BufferedImage getTileAt(int tileWidth, int tileHeight, int width, int height, int positionToFetch, int positionOffset)
    {
        int widthInPixels = width * tileWidth;
        int heightInPixels = height * tileHeight;

        int x = (positionToFetch - positionOffset - 1) % width;
        int y = (positionToFetch - positionOffset) / width;

        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }
}
