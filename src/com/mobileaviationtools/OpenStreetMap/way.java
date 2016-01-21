package com.mobileaviationtools.OpenStreetMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rob Verhoef on 28-12-2015.
 */
public class way {
    public way(Document document)
    {
        nds = new ArrayList<nd>();
        timestamp = new Date();
    }

    private Document document;

    public Integer id;
    public Boolean visible;
    public Date timestamp;
    public Integer version;
    public Integer changeset;
    public String user;
    public Integer uid;

    private ArrayList<nd> nds;
    private ArrayList<tag> tags;

    public void AddNd(Integer ref)
    {
        nd _nd = new nd();
        _nd.ref = ref;
        nds.add(_nd);
    }

    public void addTag(String k, String v) {
        tag t = new tag();
        t.k = k;
        t.v = v;
        tags.add(t);
    }

    public void setTimestamp(Date date) {
        this.timestamp = date;
    }

    public Element getElement()
    {
        Element element = document.createElement("way");

        element.setAttribute("id", id.toString());
        element.setAttribute("user", user);
        element.setAttribute("uid", uid.toString());
        element.setAttribute("visible", visible.toString());
        element.setAttribute("changeset", changeset.toString());
        element.setAttribute("version", version.toString());

        DateFormat df = new SimpleDateFormat("yyyy-MM-ddThh:mm:ssZ");
        element.setAttribute("timestamp", df.format(timestamp));

        if (nds.size()>0) {
            for (nd t : nds) {
                element.appendChild(t.getNd(document));
            }
        }

        if (tags.size()>0) {
            for (tag t : tags) {
                element.appendChild(t.getTag(document));
            }
        }

        return element;
    }
}
