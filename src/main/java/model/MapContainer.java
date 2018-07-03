package model;

public class MapContainer
{
    private static MapContainer ourInstance = new MapContainer();
    // Container with only 1 map.
    public Map map;

    public static MapContainer getInstance()
    {
        return ourInstance;
    }

    private MapContainer()
    {
        map = null;
    }

    public void setMap(Map map)
    {
        this.map = map;
    }

    public Map getMap()
    {
        return map;
    }
}
