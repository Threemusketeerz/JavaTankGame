package parser.tiled.io;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import parser.tiled.TileSet;

import javax.naming.Name;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TSXFile
{
    private File tsxFile;
    private Document doc;
    private Element root;
    private ArrayList<Node> images;
    private TileSet tileSet;

    public TSXFile(String path)
    {
        if (!path.startsWith("/"))
            tsxFile = new File(getClass().getResource("/" + path).getFile());
        else
            tsxFile = new File(getClass().getResource(path).getFile());

        images = new ArrayList<>();

        parseTSXFile();

        try
        {
            tileSet = generateTileSet();
        }
        catch(SlickException se)
        {
            se.printStackTrace();
        }
    }
    private void parseTSXFile()
    {
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(tsxFile);

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
                NamedNodeMap attributes = childNode.getAttributes();
                String name = attributes.getNamedItem("name").getNodeValue();
                int tileWidth = Integer.parseInt(attributes.getNamedItem("tilewidth").getNodeValue());
                int tileHeight = Integer.parseInt(attributes.getNamedItem("tileheight").getNodeValue());
                int tileCount = Integer.parseInt(attributes.getNamedItem("tilecount").getNodeValue());
                int columns = Integer.parseInt(attributes.getNamedItem("columns").getNodeValue());
                String inTileSet = attributes.getNamedItem("source").getNodeValue();

                try
                {
                    tileSet = new TileSet(name, tileWidth, tileHeight, tileCount, columns, inTileSet);
                } catch (SlickException e)
                {
                    e.printStackTrace();
                }
            }
            else if (nodeName.equals("image"))
                images.add(childNode);

            // recurse through childnodes
            if (childNode.hasChildNodes())
            {
                traverseNodes(childNode);
            }
        }
    }

    public TileSet generateTileSet() throws SlickException
    {
        String name = root.getAttribute("name");
        int tileWidth = Integer.parseInt(root.getAttribute("tilewidth"));
        int tileHeight = Integer.parseInt(root.getAttribute("tileheight"));
        int tilecount = Integer.parseInt(root.getAttribute("tilecount"));
        int columns = Integer.parseInt(root.getAttribute("columns"));

        String source = "";

        for (Node image : images)
        {
            NamedNodeMap attrs = image.getAttributes();
            source = attrs.getNamedItem("source").getNodeValue();
        }

        return new TileSet(name, tileWidth, tileHeight, tilecount, columns, source);
    }

    public File getTsxFile()
    {
        return tsxFile;
    }

    public Document getDoc()
    {
        return doc;
    }

    public Element getRoot()
    {
        return root;
    }

    public ArrayList<Node> getImages()
    {
        return images;
    }

    public TileSet getTileSet()
    {
        return tileSet;
    }
}
