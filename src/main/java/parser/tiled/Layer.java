package parser.tiled;

import java.util.ArrayList;

public class Layer
{
    private String name;
    private int width;
    private int height;

    private ArrayList<Tile> tiles;

    public Layer(String name, int width, int height)
    {
        this.name = name;
        this.width = width;
        this.height = height;
        tiles = new ArrayList<>();
    }

    public void addTile(Tile tile)
    {
        tiles.add(tile);
    }

    public void addTiles(ArrayList<Tile> tiles)
    {
        this.tiles = tiles;
    }

    public String getName()
    {
        return name;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void render()
    {
        for (Tile tile : tiles)
            tile.render();
    }

    public ArrayList<Tile> getTiles()
    {
        return tiles;
    }
}
