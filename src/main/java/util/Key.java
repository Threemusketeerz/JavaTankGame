package util;


/**
 * For some reason it is not possible to access Input.KEYS because it is protected.
 * To compensate for this, a replacement class has been added, with the same key
 * codes.
 */
public class Key
{
    public static final int UP      = 200;
    public static final int RIGHT   = 205;
    public static final int DOWN    = 208;
    public static final int LEFT    = 203;
    public static final int SPACE   = 57;
}
