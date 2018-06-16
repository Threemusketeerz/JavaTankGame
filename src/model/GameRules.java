package model;

public enum GameRules
{
    SPEED(2), ROTATE_SPEED(2);


    private int value;

    GameRules(int value)
    {
        this.value = value;
    }

    public int getValue() { return value; }
}

