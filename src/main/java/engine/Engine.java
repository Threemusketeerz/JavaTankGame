package engine;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import states.Game;

import java.io.File;

/**
 * This class will drive the game.
 */
public class Engine extends StateBasedGame
{
    public static final int     WIDTH = 1024;
    public static final int     HEIGHT = 768;
    public static final int     FRAME_RATE = 60;

    private static boolean running = false;
    private TiledMap map;

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
            appgc.setDisplayMode(WIDTH, HEIGHT, false);
            appgc.setTargetFrameRate(FRAME_RATE);
            appgc.setVSync(true);
            appgc.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }

    }
}
