package com.mobileaviationtools.OpenStreetMap;

import com.mobileaviationtools.AirspacesData.Airspace;
import com.mobileaviationtools.AirspacesData.Airspaces;
import com.vividsolutions.jts.geom.Coordinate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;

/**
 * Created by Rob Verhoef on 28-12-2015.
 */
public class osm {
    public osm() throws ParserConfigurationException {
        DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder =
                dbFactory.newDocumentBuilder();
        Document document = dBuilder.newDocument();
        rootElement = document.createElement("osm");
        document.appendChild(rootElement);
        version = "0.1";
        generator = "openaipAirspaceOsmGenerator";

        rootElement.setAttribute("version", version);
        rootElement.setAttribute("generator", generator);

        uid = 100000;

        nodes = new ArrayList<>();
        ways = new ArrayList<>();
    }

    public Document document;
    private Element rootElement;
    private Integer uid;

    public String version;
    public String generator;

    private ArrayList<node> nodes;
    private ArrayList<way> ways;

    public void createAirspaceOSM(Airspaces airspaces)
    {
        for (Airspace airspace : airspaces)
        {
            way w = new way(document);

            for (Coordinate coordinate : airspace.coordinates)
            {

            }
        }
    }
}
