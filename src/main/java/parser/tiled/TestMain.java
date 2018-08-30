package parser.tiled;

import engine.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import parser.tiled.io.TMXFile;

import java.io.File;

public class TestMain extends StateBasedGame
{
    public TestMain()
    {
        super("Debug Window");
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
            appgc = new AppGameContainer(new TestMain());
            appgc.setDisplayMode(Display.WIDTH, Display.HEIGHT, false);
            appgc.setTargetFrameRate(Display.FRAME_RATE);
            appgc.setVSync(true);
            appgc.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }

        TMXFile tmx = new TMXFile("Maps/TestMapFinal.tmx");
        TiledMap tiledMap = tmx.generateTiledMap();
        tiledMap.render();
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException
    {

    }
}
