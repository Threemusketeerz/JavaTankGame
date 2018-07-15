package manager;

import model.Tank;
import model.Camera;
import model.Constraint;
import model.TankContainer;

import java.awt.image.BufferedImage;

public class TankManager implements AssetManager
{

    /**
     * Constrains the tank, gives it boundaries to operate within.
     * @param tank          Tank to constrian
     * @param constraint    The constraint itself, which are just 4 x, y coordinates.
     */
    public void constrain(Tank tank, Constraint constraint)
    {
        tank.setConstraint(constraint);
    }

    /**
     * Attaches a camera to the tank. For now the Tanks can only have one.
     * @param tank      Tank to give camera.
     * @param camera    Camera to attach.
     */
    public void attachCamera(Tank tank, Camera camera)
    {
        tank.setCamera(camera);
    }

    @Override
    public BufferedImage findAsset(String name)
    {
        return TankContainer.getInstance().findAsset(name);
    }
}
