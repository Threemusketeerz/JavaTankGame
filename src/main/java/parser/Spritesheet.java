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
        // attempt 1
//        int x = (((positionToFetch * tileWidth) - 1) % widthInPixels)/tileWidth;
        // attempt 2
//        int x = tileWidth / positionToFetch;

        int y = (positionToFetch - positionOffset) / width;
//        if (y > 0)
//            y -= 1;

        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }
}
