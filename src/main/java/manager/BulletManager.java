package manager;

import model.BulletContainer;
import org.newdawn.slick.Image;

import java.awt.image.BufferedImage;

public class BulletManager implements AssetManager
{

    public Image findAsset(String name)
    {
        return BulletContainer.getInstance().findAsset(name);
    }

    public void loadInAssets(String location, boolean clearAssets)
    {
        BulletContainer.getInstance().loadInAssets(location, clearAssets);
    }

}
