package model;


import java.util.ArrayList;

public class TankContainer extends AssetContainer
{
    private static TankContainer instance = new TankContainer();
    // Relative location of tanks
    private static final String TANKS = "/SimpleTanks/Tanks/";

    private ArrayList<Tank> tanks;

    private TankContainer()
    {
        tanks = new ArrayList<>();
        loadInAssets(TANKS, true);
    }

    public static TankContainer getInstance() { return instance; }
}
