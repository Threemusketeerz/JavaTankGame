package model;

import parser.Spritesheet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.io.IOException;

public class TankMap
{
    private String path;
    private int width, height, tileWidth, tileHeight;
    private JSONArray tilesets;
    private JSONArray layers;

    public TankMap(String path) throws ParseException, IOException
    {
        this.path = path;

        update(path);
    }

    public String getPath() { return path; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getTileWidth() { return tileWidth; }
    public int getTileHeight() { return tileHeight; }
    public JSONArray getLayers() { return layers; }

    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }

    /**
     * Reload the json file.
     */
    public void update(String path) throws ParseException, IOException
    {
        this.path = path;

        // Parse all the data from the jsonFile.
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject)parser.parse(new FileReader(path));
        layers = (JSONArray) jsonObject.get("layers");
        tileWidth = (int) ((long)jsonObject.get("tilewidth"));
        tileHeight = (int) ((long)jsonObject.get("tilewidth"));
        tilesets = (JSONArray)jsonObject.get("tilesets");

        // Width and height are a bit misleading atm.
        width = (int) ((long)jsonObject.get("width"));
        height = (int) ((long)jsonObject.get("height"));

    }
}
