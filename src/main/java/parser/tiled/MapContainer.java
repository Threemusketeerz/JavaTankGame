package parser.tiled;

import java.util.ArrayList;
import java.util.HashMap;

public class MapContainer
{
    public static final MapContainer instance = new MapContainer();

    // Maps with bound layers.
    private HashMap<String, ArrayList<Layer>> maps;

    private MapContainer()
    {
        maps = new HashMap<>();
    }

    public MapContainer getInstance()
    {
        return instance;
    }
}
