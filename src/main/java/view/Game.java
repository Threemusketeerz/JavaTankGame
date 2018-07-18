package view;

import engine.GameEngine;
//import states.PlayState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// TODO Rename to Game.
public class Game extends GameEngine
{
    public static final int         WIDTH = 1200, HEIGHT = 800;
    // Framerate. Essentially, 1 second = 1000ms / FRAME_RATE = DELAY.
    public static final int         FRAME_RATE = 60;
    private static final int        DELAY = 1000/FRAME_RATE;
    private Timer                   timer;



    public Game()
    {
        // Init playstate
//        changeState(PlayState.getInstance());
        getState().init(WIDTH, HEIGHT);

        addKeyListener(new TAdapter());

        setVisible(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        setDoubleBuffered(true);
        timer = new Timer(DELAY, this);
        timer.start();
    }

//    public void update()
//    {
//        repaint();
//    }

    @Override
    public void update()
    {
        repaint();
    }


//    @Override
//    public void handleEvents(ActionEvent e)
//    {
//        getState().handleEvents(e);
////        getState().draw((Graphics2D)getGraphics());
//        // Location update.
//    }


    public class TAdapter extends KeyAdapter
    {
        public void keyPressed(KeyEvent e)
        {
            getState().keyPressed(e);
        }

        public void keyReleased(KeyEvent e)
        {
            getState().keyReleased(e);
        }
    }

}
