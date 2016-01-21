package com.mobileaviationtools.OpenStreetMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by Rob Verhoef on 28-12-2015.
 */
public class nd {
    public Integer ref;

    public Element getNd(Document document)
    {
        Element element = document.createElement("nd");
        element.setAttribute("ref", ref.toString());
        return element;
    }
}
