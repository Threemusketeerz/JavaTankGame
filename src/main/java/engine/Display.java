package engine;

public class Display
{
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final int FRAME_RATE = 60;
    public static final float SCALE_FACTORS[] = {0.8f, 1f, 1.2f, 1.4f, 1.6f, 1.8f, 2f};
    public static int scale = 0;


    public static float getScale()
    {
        return SCALE_FACTORS[scale];
    }
}
