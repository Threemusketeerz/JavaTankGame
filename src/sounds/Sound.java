package sounds;
import java.io.FileInputStream;
import java.io.InputStream;

import javazoom.jl.player.Player;
import javazoom.jl.decoder.JavaLayerException;

/**
 * Write a description of class SoundPlayer here.
 *
 * @author Bjarke Sporring
 * @version 0.1
 */
public class Sound extends Thread
{
    private String audioFile;
    private FileInputStream fileStream;
    private Player player;

    public Sound(String audioFile)
    {
         this.audioFile = audioFile;
         // Fetch resource InputStream directly
         player = setupPlayer(getInputStream());
    }

    protected InputStream getInputStream()
    {
        return getClass().getResourceAsStream(audioFile);
    }

    protected Player setupPlayer(InputStream stream)
    {
        Player tempPlayer = null;
        try
        {
            tempPlayer = new Player(stream);
        }
        catch (JavaLayerException e)
        {
            System.out.println("Player failed to load InputStream");
            System.out.println(e);
        }
        return tempPlayer;
    }

    public void run()
    {
        try
        {
            player.play();
        }
        catch (JavaLayerException e)
        {
            System.out.println("Player failed to play InputStream");
            e.printStackTrace();
        }
        catch (Exception e)
        {
            return;
        }
    }
}
