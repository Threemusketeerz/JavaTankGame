package manager;

import model.AssetContainer;

import java.awt.image.BufferedImage;

public interface AssetManager
{
    BufferedImage findAsset(String name);
}
