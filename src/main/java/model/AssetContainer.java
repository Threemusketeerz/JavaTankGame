package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public abstract class AssetContainer
{
    private HashMap<String, BufferedImage> assets;

    public AssetContainer()
    {
        assets = new HashMap<>();
    }
    /**
     * Simply loads in the assets for the Bullets
     * @param location      Relative location of the assets.
     * @param clearAssets   Whether or not to clear the current storage
     */
    public void loadInAssets(String location, boolean clearAssets)
    {
        File dir = new File(getClass().getResource(location).toString().replace("file:", ""));
        if (clearAssets)
            assets.clear();

        if (dir.isDirectory())
        {
            File[] images = dir.listFiles();

            System.out.println("Loading in assets from folder: " + location);
            for (int i = 0; i < images.length; i++)
            {

                System.out.println("Loading in : " + images[i]);
                assets.put(images[i].getName(), Sprite.loadImage(images[i].toString()));
            }
        }
    }

    public BufferedImage findAsset(String name)
    {
        return assets.get(name);
    }


    public Set<String> keySet()
    {
        return assets.keySet();
    }

    public Collection<BufferedImage> values()
    {
        return assets.values();
    }
}
