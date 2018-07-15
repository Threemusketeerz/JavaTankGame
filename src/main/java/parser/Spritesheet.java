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

    public BufferedImage getTileAt(int tileWidth, int tileHeight, int width, int height, int positionToFetch)
    {
        int x = (positionToFetch  - 1) % width;
        int y = (positionToFetch) / width;

        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }
}
