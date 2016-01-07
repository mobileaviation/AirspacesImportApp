package com.mobileaviationtools.OpenStreetMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by Rob Verhoef on 28-12-2015.
 */
public class tag {
    public String k;
    public String v;

    public Element getTag(Document document)
    {
        Element element = document.createElement("tag");
        element.setAttribute("k", k);
        element.setAttribute("v", v);
        return element;
    }
}


