package view; import model.TankMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import parser.Spritesheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MapView
{
    private TankMap tankMap;
    private Spritesheet spriteSheet;

    public MapView(TankMap tankMap, Spritesheet spriteSheet)
    {
        this.tankMap = tankMap;
        this.spriteSheet = spriteSheet;
    }

    public void paintComponent(Graphics g) {
        final Graphics2D g2d = (Graphics2D) g.create();
        final Rectangle clip = g2d.getClipBounds();

        // Draw a gray background
        g2d.setPaint(new Color(100, 100, 100));
//
        g2d.fill(clip);
        // Draw each map layer
        int layerCount = ((JSONArray)tankMap.getLayers()).size();

        //System.out.println("Layers: " + layerCount);

        for (int i = 0; i < layerCount; i++)
        {
            JSONObject layer = (JSONObject) tankMap.getLayers().get(i);
//            long[] data = (long[]) layer.get("data");
            JSONArray data = (JSONArray) layer.get("data");

            for (int j = 0; j < data.size(); j++)
            {
                int mapWidth = tankMap.getWidth() * tankMap.getTileWidth();
                int mapHeight = tankMap.getHeight() * tankMap.getTileHeight();
                // to get point of the j in an x and y format we need to find the data first.
                int pointInPixelsX = j * tankMap.getTileWidth();
                int pointInPixelsY = j * tankMap.getTileHeight();
                int posX = pointInPixelsX % mapWidth;
                int posY = (pointInPixelsY / mapHeight) * tankMap.getTileHeight();

                int dataPoint = (int)((long)data.get(j));

                System.out.println(
                        "Data Point: " + dataPoint +
                        "\nJ value: " + j);

                BufferedImage tile = spriteSheet.getTileAt(
                        tankMap.getTileWidth(), tankMap.getTileHeight(),
                        8, 4,
                        dataPoint, 0);

                g2d.drawImage(tile, posX, posY, null);

            }
        }
    }
}
