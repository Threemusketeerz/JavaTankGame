package model;

import java.util.ArrayList;

public class BulletContainer
{
    private static BulletContainer instance = new BulletContainer();
    private ArrayList<Bullet> bullets;
    private BulletContainer()
    {
        bullets = new ArrayList<>();
    }

    public static BulletContainer getInstance()
    {
        if (instance == null)
            instance = new BulletContainer();
        return instance;
    }

    public void add(Bullet bullet)
    {
        bullets.add(bullet);
    }

    public void delete(Bullet bullet)
    {
        bullets.remove(bullet);
    }

    public ArrayList<Bullet> getBullets()
    {
        return bullets;
    }
}

