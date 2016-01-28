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
import java.util.Date;

/**
 * Created by Rob Verhoef on 28-12-2015.
 */
public class osm {
    public osm() throws ParserConfigurationException {
        DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder =
                dbFactory.newDocumentBuilder();
        document = dBuilder.newDocument();
        rootElement = document.createElement("osm");
        document.appendChild(rootElement);
        version = "0.1";
        generator = "openaipAirspaceOsmGenerator";

        rootElement.setAttribute("version", version);
        rootElement.setAttribute("generator", generator);

        uid = 100000;
        way_id = 20000;
        node_id = 30000;
        changeset = 1;

        nodes = new ArrayList<>();
        ways = new ArrayList<>();
    }

    public Document document;
    private Element rootElement;
    private Integer uid;
    private Integer way_id;
    private Integer node_id;
    private Integer changeset;

    public String version;
    public String generator;

    private ArrayList<node> nodes;
    private ArrayList<way> ways;

    public void createAirspaceOSM(Airspaces airspaces)
    {
        for (Airspace airspace : airspaces)
        {
            way w = new way(document);
            w.id = way_id;
            way_id++;
            w.uid = uid;
            uid++;
            w.visible = true;
            w.changeset = changeset;
            w.user = "rob";
            w.version = 1;
            w.setTimestamp(new Date());

            for (Coordinate coordinate : airspace.coordinates)
            {
                w.addNd(node_id);

                node _node = new node(document);
                _node.id = node_id;
                _node.uid = uid;
                _node.user = "rob";
                _node.visible = true;
                _node.version = 1;
                _node.lat = (float)coordinate.y;
                _node.lon = (float)coordinate.x;
                _node.changeset = 1;

                nodes.add(_node);

                node_id++;
                uid++;
            }

            w.addTag("airspacecategory", airspace.Category.toString());
            w.addTag("name", airspace.Name);
            w.addTag("country", airspace.Country);
            w.addTag("version", airspace.Version);
            w.addTag("bottomlimit", airspace.AltLimit_Bottom.toString());
            w.addTag("bottomlimitunit", airspace.AltLimit_Bottom_Unit.toString());
            w.addTag("bottomlimitref", airspace.AltLimit_Bottom_Ref.toString());
            w.addTag("toplimit", airspace.AltLimit_Top.toString());
            w.addTag("toplimitunit", airspace.AltLimit_Top_Unit.toString());
            w.addTag("toplimitref", airspace.AltLimit_Top_Ref.toString());

            ways.add(w);
        }

        for(way _way: ways)
        {
            Element ew = _way.getElement();
            rootElement.appendChild(ew);
        }

        for (node _node: nodes)
        {
            Element en = _node.getElement();
            rootElement.appendChild(en);
        }
    }
}
