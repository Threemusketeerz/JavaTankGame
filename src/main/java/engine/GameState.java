package engine;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public interface GameState
{
    void init(final int width, final int height);
    void cleanup();

    void pause();
    void resume();

    void handleEvents(ActionEvent e);
    void keyPressed(KeyEvent e);
    void keyReleased(KeyEvent e);
    void update();
    void draw(Graphics2D g2d);
}
