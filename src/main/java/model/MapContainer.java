package model;

import org.newdawn.slick.tiled.TiledMap;

public class MapContainer
{
    private static MapContainer ourInstance = new MapContainer();
    // Container with only 1 map.
    public TiledMap map;

    public static MapContainer getInstance()
    {
        return ourInstance;
    }

    private MapContainer()
    {
        map = null;
    }

    public void setMap(TiledMap map)
    {
        this.map = map;
    }

    public TiledMap getMap()
    {
        return map;
    }
}
