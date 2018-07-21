package model;


import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BulletContainer extends AssetContainer
{
    private static BulletContainer instance = new BulletContainer();

    private static final String BULLETS = "/SimpleTanks/Bullets/";
    // For storing the bullets themselves.
    private ArrayList<Bullet> bullets;
    // For storing bullet assets.
    private ArrayList<BufferedImage> assets;

    private BulletContainer()
    {
        bullets = new ArrayList<>();
        loadInAssets(BULLETS, true);
    }

    public static BulletContainer getInstance()
    {
        if (instance == null)
            instance = new BulletContainer();
        return instance;
    }

    public void addBullet(Bullet bullet)
    {
        bullets.add(bullet);
    }
    public void addAsset(BufferedImage asset) { assets.add(asset); }

    public void delete(Bullet bullet)
    {
        bullets.remove(bullet);
    }

    public ArrayList<Bullet> getBullets()
    {
        return bullets;
    }
}

