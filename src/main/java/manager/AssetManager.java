package manager;

import model.AssetContainer;
import org.newdawn.slick.Image;

import java.awt.image.BufferedImage;

public interface AssetManager
{
    Image findAsset(String name);
    void loadInAssets(String location, boolean clearAssets);
}
