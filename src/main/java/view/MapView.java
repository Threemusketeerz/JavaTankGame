package view;

import model.TankMap;
import model.Tank;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import parser.Spritesheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapView
{
    private TankMap         tankMap;
    private Spritesheet     spriteSheet;

    public MapView(TankMap tankMap, Spritesheet spriteSheet)
    {
        this.tankMap = tankMap;
        this.spriteSheet = spriteSheet;
    }

    /**
     * Paints the entire map from the JSON File.
     * NOTE: This should be constrained to the player position. In other words - a camera is needed.
     * @param g     Graphics context to draw in.
     */
    public void paintComponent(Graphics g, Tank tank) {
        final Graphics2D g2d = (Graphics2D) g.create();
        final Rectangle clip = g2d.getClipBounds();

        // Draw a gray background
        g2d.setPaint(new Color(100, 100, 100));
//
        g2d.fill(clip);
        // Draw each map layer
        int layerCount = tankMap.getLayers().size();

        //System.out.println("Layers: " + layerCount);

        // Draws entire map.
        for (int i = 0; i < layerCount; i++)
        {
            JSONObject layer = (JSONObject) tankMap.getLayers().get(i);
//            long[] data = (long[]) layer.get("data");
            JSONArray data = (JSONArray) layer.get("data");

            ArrayList<BufferedImage> tilesToDraw = new ArrayList<>();

            // Gather all the tiles we want drawn.
            for (int j = 0; j < data.size(); j++)
            {
                // Width in pixels
                int mapWidth = tankMap.getWidth() * tankMap.getTileWidth();

                // Height in pixels
                int mapHeight = tankMap.getHeight() * tankMap.getTileHeight();

                // Current j position, mapped to pixels
                int pointInPixelsX = j * tankMap.getTileWidth();
                int pointInPixelsY = j * tankMap.getTileHeight();

                // Position x and y for drawing tile calculated with pixels
                int posX = pointInPixelsX % mapWidth;
                int posY = (pointInPixelsY / mapHeight) * tankMap.getTileHeight();

                // Tile to fetch
                int dataPoint = (int)((long)data.get(j));

//                System.out.println(
//                        "Data Point: " + dataPoint +
//                        "\nJ value: " + j);


                // Draw from
                double tileOffset = 2 * tankMap.getTileWidth();
                double xCamera = tank.getCamera().getOffset().getX();
                double yCamera = tank.getCamera().getOffset().getY();

                // Data to fetch the rendering area, should be a square around the player position
                // TODO Switch the tileOffset to - -> + and vice versa
                double drawStartPointX = (xCamera - clip.getWidth()) + tileOffset;
                double drawStartPointY = (yCamera - clip.getHeight()) + tileOffset;
                double drawEndPointX = (xCamera + clip.getWidth()) - tileOffset;
                double drawEndPointY = (yCamera + clip.getHeight()) - tileOffset;

                if (posX >= drawStartPointX
                        && posY >= drawStartPointY
                        && posX <= drawEndPointX
                        && posY <= drawEndPointY)
                {
                    // Actual tile.
                    // TODO fix hardcoded values.
                    BufferedImage tile = spriteSheet.getTileAt(
                            tankMap.getTileWidth(), tankMap.getTileHeight(),
                            // Spritesheet width and height. Not to be confused with the Tilesheet
                            8, 4,
                            dataPoint, 0);
                    tilesToDraw.add(tile);

                    // Relocate draw location, to fit start of the draw point.
                    int xOffset = (int)tank.getCamera().getOffset().getX();
                    int yOffset = (int)tank.getCamera().getOffset().getY();
//                    System.out.println("x offset:           " + xOffset);
//                    System.out.println("y offset:           " + yOffset);
//                    System.out.println("xDrawPoint:         " +  (posX - xOffset));
//                    System.out.println("yDrawPoint:         " +  (posY - yOffset));
                    g2d.drawImage(tile, posX - xOffset, posY - yOffset, null);
                }
                    // old



//                if (pointInPixels)

            }
            // Empty the arrayList.
//            tilesToDraw.clear();
        }
    }
}
