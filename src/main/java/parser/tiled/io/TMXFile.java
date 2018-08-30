package parser.tiled.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import parser.tiled.Layer;
import parser.tiled.Tile;
import parser.tiled.TiledMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Loads in TMX file following the specification.
 */
public class TMXFile
{

    private String path;
    private File tmxFile;
    private TSXFile tsxFile;

    private Element root;

    // Lists for ALL nodes. It essentially equalizes everything.
    // This is not necessarily good for everything. Such as Tiles, since we wanna know which layer
    // it belongs to.
    private ArrayList<Node> tilesets;
    private ArrayList<Node> layers;
    private ArrayList<Node> data;
    private ArrayList<Node> tiles;
    private ArrayList<Node> objectGroups;
    private ArrayList<Node> objects;

    public TMXFile(String path)
    {
        this.path = path;

        if (!path.startsWith("/"))
            this.tmxFile = new File(getClass().getResource("/" + path).getFile());
        else
            this.tmxFile = new File(getClass().getResource(path).getFile());

        this.tiles = new ArrayList<>();
        this.tilesets = new ArrayList<>();
        this.layers = new ArrayList<>();
        this.data = new ArrayList<>();
        this.objectGroups = new ArrayList<>();
        this.objects = new ArrayList<>();

        parseTMXFile();

        tsxFile = new TSXFile("Maps/" + getTSXSource());
    }

    private void parseTMXFile()
    {
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(tmxFile);

            doc.getDocumentElement().normalize();

            root = doc.getDocumentElement();

            traverseNodes(root);
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }

    }

    public void readTileset()
    {

    }

    public void render()
    {
    }

    /**
     * Recursively loads in the file into memory according to TMX file specification.
     * Theres is no protection from duplicating data, so be careful
     * @param node
     */
    private void traverseNodes(Node node)
    {
        NodeList childNodes = node.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node childNode = childNodes.item(i);
            String nodeName = childNode.getNodeName().trim();

            if (nodeName.equals("tileset"))
            {
                System.out.println("Adding " + nodeName);
                tilesets.add(childNode);
            }
            else if (nodeName.equals("layer"))
            {
                layers.add(childNode);
            }
            else if (nodeName.equals("data"))
            {
                System.out.println("Adding " + nodeName);
                data.add(childNode);
            }
            else if (nodeName.equals("tile"))
            {
                System.out.println("Adding " + nodeName);
                tiles.add(childNode);
            }
            else if (nodeName.equals("objectgroup"))
            {
                System.out.println("Adding " + nodeName);
                objectGroups.add(childNode);
            }
            else if (nodeName.equals("object"))
            {
                System.out.println("Adding " + nodeName);
                objects.add(childNode);
            }

            // recurse through childnodes
            if (childNode.hasChildNodes())
            {
                traverseNodes(childNode);
            }
        }
    }

    public void reload()
    {
        cleanUp();
        traverseNodes(root);
    }

    public void cleanUp()
    {
        tilesets.clear();
        layers.clear();
        data.clear();
        tiles.clear();
        objectGroups.clear();
        objects.clear();
    }

    public String getTSXSource()
    {
        return tilesets.get(0).getAttributes().getNamedItem("source").getNodeValue();
    }

    public TiledMap generateTiledMap()
    {
        NamedNodeMap attributes = root.getAttributes();
        int width = Integer.parseInt(attributes.getNamedItem("width").getNodeValue());
        int height = Integer.parseInt(attributes.getNamedItem("height").getNodeValue());
        int tileWidth = Integer.parseInt(attributes.getNamedItem("tilewidth").getNodeValue());
        int tileHeight = Integer.parseInt(attributes.getNamedItem("tileheight").getNodeValue());
        // TODO Fix stupid 0 index.
        // This is only possible because there aren't more than one tilesets atm
        String tileset = "";
        if (tilesets.size() > 0)
            tileset = tilesets.get(0).getAttributes().getNamedItem("source").getNodeValue();

        TiledMap outTiledMap = new TiledMap(width, height, tileWidth, tileHeight, tileset, generateLayers());
        return outTiledMap;
    }

    public ArrayList<Layer> generateLayers()
    {
        ArrayList<Layer> outLayers = new ArrayList<>();

        for (Node layer : layers)
        {
            NamedNodeMap attributes = layer.getAttributes();
            String layerName = attributes.getNamedItem("name").getNodeValue();
            int layerWidth = Integer.parseInt(attributes.getNamedItem("width").getNodeValue());
            int layerHeight = Integer.parseInt(attributes.getNamedItem("height").getNodeValue());
            Layer outLayer = new Layer(layerName, layerWidth, layerHeight);

            outLayer.addTiles(generateTilesForLayer(layer));

            outLayers.add(outLayer);
        }


        return outLayers;
    }

    public ArrayList<Tile> generateTilesForLayer(Node layer)
    {
        ArrayList<Tile> outTiles = new ArrayList<>();

        NodeList layerChildren = layer.getChildNodes();

        for (int i = 0; i < layerChildren.getLength(); i++)
        {
            Node data = layerChildren.item(i);
            NodeList tileNodes = data.getChildNodes();

            for (int tileIndex = 0; tileIndex < tileNodes.getLength(); tileIndex++)
            {
                Node tileNode = tileNodes.item(tileIndex);

                int gid = Integer.parseInt(tileNode.getAttributes().getNamedItem("gid").getNodeValue());

                Tile outTile = new Tile(gid);
                outTiles.add(outTile);
            }
        }
        return outTiles;
    }
}
