package engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public abstract class GameEngine extends JPanel implements ActionListener
{
    private boolean                 running;
    private ArrayList<GameState>    states;
    private GameState               state;

    public GameEngine()
    {
        running = true;
        states = new ArrayList<>();
    }

    public void paintComponent(Graphics g)
    {
//        System.out.println("Drawing");
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        draw(g2d);
        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        handleEvents(e);
    }

    public void init()
    {

    }

    public void changeState(GameState state) { this.state = state; }
    public void pushState(GameState state)
    {
        states.add(state);
    }

    // Remove the first state
    public GameState popState() { return states.remove(0); }

    public void handleEvents(ActionEvent e)
    {
        paintComponent(getGraphics());
        state.handleEvents(e);
    }

    public abstract void update();

    public void draw(Graphics2D g2d)
    {
        state.draw(g2d);
    }

    public boolean running() { return running; }
    public void quit() { this.running = false; }


    public ArrayList<GameState> getStates() { return states; }
    public GameState            getState() { return state; }

}
