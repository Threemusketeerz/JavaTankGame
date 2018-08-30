package parser.tiled;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.io.File;

public class TileSet
{
    private String name;
    private int tileWidth;
    private int tileHeight;
    private int tileCount;
    private int columns;

    private SpriteSheet tileSheet;

    public TileSet(String name, int tileWidth, int tileHeight, int tileCount, int columns, SpriteSheet tileSheet)
    {
        this.name = name;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tileCount = tileCount;
        this.columns = columns;
        this.tileSheet = tileSheet;
    }

    public TileSet(String name, int tileWidth, int tileHeight, int tileCount, int columns, String tileSheet) throws SlickException
    {
        this(name, tileWidth, tileHeight, tileCount, columns, new SpriteSheet(removePrefix(tileSheet), tileWidth, tileHeight));
    }

    public static String removePrefix(final String path)
    {
        boolean found = false;
        int foundIndex = -1;

        for (int i = 0; i < path.length() && !found; i++)
        {
            char c = path.charAt(i);
            if (Character.isLowerCase(c) || Character.isUpperCase(c))
            {
                found = true;
                foundIndex = i;
            }
        }

        return path.substring(foundIndex);
    }

    public String getName()
    {
        return name;
    }

    public int getTileWidth()
    {
        return tileWidth;
    }

    public int getTileHeight()
    {
        return tileHeight;
    }

    public int getTileCount()
    {
        return tileCount;
    }

    public int getColumns()
    {
        return columns;
    }

    public SpriteSheet getTileSheet()
    {
        return tileSheet;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTileWidth(int tileWidth)
    {
        this.tileWidth = tileWidth;
    }

    public void setTileHeight(int tileHeight)
    {
        this.tileHeight = tileHeight;
    }

    public void setTileCount(int tileCount)
    {
        this.tileCount = tileCount;
    }

    public void setColumns(int columns)
    {
        this.columns = columns;
    }

    public void setTileSheet(SpriteSheet tileSheet)
    {
        this.tileSheet = tileSheet;
    }
}
