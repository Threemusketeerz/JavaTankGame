package manager;

import model.BulletContainer;

import java.awt.image.BufferedImage;

public class BulletManager implements AssetManager
{

    public BufferedImage findAsset(String name)
    {
        return BulletContainer.getInstance().findAsset(name);
    }
}
