package parser.tiled;

import parser.tiled.io.TSXFile;

import java.util.ArrayList;

public class TiledMap
{
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private TSXFile tilesetFile;
    private TileSet tileset;
    private ArrayList<Layer> layers;

    public TiledMap(int width, int height, int tileWidth, int tileHeight, TSXFile tilesetFile, ArrayList<Layer> layers)
    {
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tilesetFile = tilesetFile;
        this.layers = layers;
    }


    public TiledMap(int width, int height, int tileWidth, int tileHeight, String tileset, ArrayList<Layer> layers)
    {
        this(width, height, tileWidth, tileHeight,
                new TSXFile(ClassLoader.getSystemClassLoader()
                        .getResource("Maps/" + tileset).getFile()),
                layers);
    }

    private void generateImagesForTiles()
    {
        for (Layer layer : layers)
        {
            ArrayList<Tile> tiles = layer.getTiles();
        }
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getTileWidth()
    {
        return tileWidth;
    }

    public int getTileHeight()
    {
        return tileHeight;
    }

    public void render(int x, int y)
    {
        int xScale = tileset.getTileWidth();
        int yScale = tileset.getTileHeight();
        // Populate each tile with their image.
        // Now all we have to do is iterate through the array an render the images
        for (Layer layer : layers)
        {
            System.out.println("Goind through layers " + layer);
            tileset.getTileSheet().startUse();
            for (Tile tile : layer.getTiles())
            {
                System.out.println("Getting tiles " + tile);
                int xTile = tile.getX(tileset.getColumns());
                int yTile = tile.getY(tileset.getColumns());
                System.out.println("Rendering xTile " + xTile);
                System.out.println("Rendering yTile " + yTile);
                System.out.println("-----");
                tileset.getTileSheet().renderInUse(x, y, xTile, yTile);
                x += xScale;
                y += yScale;
            }
            tileset.getTileSheet().endUse();
        }

    }

    public void render()
    {
        render(0, 0);
    }

    public ArrayList<Layer> getLayers()
    {
        return layers;
    }
}
