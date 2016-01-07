package com.mobileaviationtools.OpenStreetMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.jnlp.IntegrationService;
import javax.print.DocFlavor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rob Verhoef on 28-12-2015.
 */
public class node {
    public node(Document document) {
        this.document = document;
        tags = new ArrayList<tag>();
        timestamp = new Date();
    }

    private Document document;

    private ArrayList<tag> tags;

    public void addTag(String k, String v) {
        tag t = new tag();
        t.k = k;
        t.v = v;
        tags.add(t);
    }

    public Integer id;
    public Float lat;
    public Float lon;
    public String user;
    public Integer uid;
    public Boolean visible;
    public Integer version;
    public Integer changeset;
    private Date timestamp;

    public void setTimestamp(Date date) {
        this.timestamp = date;
    }

    public Element getElement()
    {
        Element element = document.createElement("node");

        element.setAttribute("id", id.toString());
        element.setAttribute("lat", lat.toString());
        element.setAttribute("lon", lon.toString());
        element.setAttribute("user", user);
        element.setAttribute("uid", uid.toString());
        element.setAttribute("visible", visible.toString());
        element.setAttribute("changeset", changeset.toString());
        element.setAttribute("version", version.toString());

        DateFormat df = new SimpleDateFormat("yyyy-MM-ddThh:mm:ssZ");
        element.setAttribute("timestamp", df.format(timestamp));

        if (tags.size()>0) {
            for (tag t : tags) {
                element.appendChild(t.getTag(document));
            }
        }

        return element;
    }

}
