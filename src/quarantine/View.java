package quarantine;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.IOException;

public class View extends JPanel
{
    private int playerX, playerY;
    private int worldSizeW, worldSizeH;
    private int cameraX, cameraY, cameraWidth, cameraHeight;
    private int offsetX, offsetY;
    private Image view;

    public View(int playerX, int playerY,
                int worldSizeW, int worldSizeH,
                int cameraWidth, int cameraHeight,
                Board board)
    {
        this.board = board;

        // How much of a view do we have?
        this.cameraWidth = cameraWidth;
        this.cameraHeight = cameraHeight;

        // Offset til center player in View view.

        // set View limits.
        this.worldSizeW = worldSizeW;
        this.worldSizeH = worldSizeH;


//        view = board.getLoadedImage();

        update(playerX, playerY);
        setPreferredSize(new Dimension(cameraWidth, cameraHeight));
    }

    public void update(int playerX, int playerY)
    {
        calculateOffset();
        calculatePosition(playerX, playerY);
    }

    private void calculateOffset()
    {
        int tempOffsetX = cameraWidth / 2;
        int tempOffsetY = cameraHeight / 2;
        int boardWidth = board.getLoadedImage().getWidth();
        int boardHeight = board.getLoadedImage().getHeight();

        if (tempOffsetX < 0)
            offsetX = 0;
        else if (tempOffsetX > boardWidth)
            offsetX -= board.getLoadedImage().getWidth();
        else
            offsetX = tempOffsetX;

        if (tempOffsetY < 0)
            offsetY = 0;
        else if(tempOffsetY > boardHeight)
            offsetY -= boardHeight;
        else
            offsetY = tempOffsetY;

    }


    /**
     * Calculates the position of the View in relativity to the board.
     * It ensures that BufferedImage.subImage(...) does not fail
     * @param playerX   The X position of the player.
     * @param playerY   the Y position of the player.
     */
    private void calculatePosition(int playerX, int playerY)
    {
        calculateOffset();
        int tempCameraX = playerX - offsetX;
        int tempCameraY = playerY - offsetY;

        int boardWidth = board.getLoadedImage().getWidth();
        int boardHeight = board.getLoadedImage().getHeight();
        if(tempCameraX < 0)
            cameraX = 0;
        else if (tempCameraX > boardWidth - offsetX)
            cameraX = boardWidth - offsetX;
        else
            cameraX = playerX - offsetX;

        if (tempCameraY < 0)
            cameraY = 0;
        else if (tempCameraY > boardHeight - offsetY)
            cameraY = boardHeight - offsetY;
        else
            cameraY = playerY - offsetY;

        view = board.getLoadedImage().getSubimage(cameraX, cameraY, cameraWidth, cameraHeight);
//        view = view.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(view, 0, 0, this);
    }

}
