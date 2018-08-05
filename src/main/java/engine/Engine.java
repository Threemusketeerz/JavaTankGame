package engine;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import states.Game;

import java.io.File;

/**
 * This class will drive the game.
 */
public class Engine extends StateBasedGame
{

    private static boolean running = false;

    public Engine()
    {
        super("Java Tank Game");
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException
    {
        running = true;
        addState(new Game());

    }

    public static void main(String[] args)
    {
        File f = new File("natives");
        System.setProperty("java.library.path", f.getAbsolutePath());
        if (f.exists())
        {
            System.setProperty("org.lwjgl.librarypath", f.getAbsolutePath());
        }

        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Engine());
            appgc.setDisplayMode(Display.WIDTH, Display.HEIGHT, false);
            appgc.setTargetFrameRate(Display.FRAME_RATE);
            appgc.setVSync(true);
            appgc.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }

    }
}
