package model;

public class MapContainer
{
    private static MapContainer ourInstance = new MapContainer();
    // Container with only 1 map.
    public TankMap map;

    public static MapContainer getInstance()
    {
        return ourInstance;
    }

    private MapContainer()
    {
        map = null;
    }

    public void setMap(TankMap map)
    {
        this.map = map;
    }

    public TankMap getMap()
    {
        return map;
    }
}
