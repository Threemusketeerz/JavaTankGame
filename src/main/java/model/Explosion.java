package model;


import org.newdawn.slick.geom.Circle;

public class Explosion extends Circle
{
    private float startRadius;
    private float expandRate;
    private float expandMax;
    private boolean finishedExpanding;

    public Explosion(float centerX, float centerY, float radius)
    {
        super(centerX, centerY, radius);
        startRadius = radius;
        expandRate = .1f;
        expandMax = radius + 50f;
        finishedExpanding = false;
    }

    public Explosion(float centerX, float centerY)
    {
        this(centerX, centerY, 80f);
    }

    public void expand(int delta)
    {
        if (getRadius() > startRadius + expandMax)
            finishedExpanding = true;
        else
        {
            setX(getX() - (expandRate * delta));
            setY(getY() - (expandRate * delta));

            setRadius(getRadius() + (expandRate * delta));
        }
    }

    public boolean isFinishedExpanding()
    {
        return finishedExpanding;
    }
}
