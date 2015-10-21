package com.mobileaviationtools.AirspacesData;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.util.ArrayList;

/**
 * Created by Rob Verhoef on 20-10-2015.
 */
public class Airspace {
    public Airspace()
    {
        coordinates = new ArrayList<Coordinate>();
    }

    public AirspaceCategory Category;
    public String Version;
    public Integer ID;
    public String Country;
    public String Name;
    public Integer AltLimit_Top;
    public AltitudeUnit AltLimit_Top_Unit;
    public AltitudeReference AltLimit_Top_Ref;
    public Integer AltLimit_Bottom;
    public AltitudeUnit AltLimit_Bottom_Unit;
    public AltitudeReference AltLimit_Bottom_Ref;
    private com.vividsolutions.jts.geom.Geometry Geometry;
    public ArrayList<Coordinate> coordinates;

    public Geometry getGeometry()
    {
        if (Geometry == null) {
            Coordinate[] c = coordinates.toArray(new Coordinate[coordinates.size()]);
            Geometry = new GeometryFactory().createPolygon(c);
        }
        return Geometry;
    }

    public void setGeometry(Geometry geometry)
    {
        this.Geometry = geometry;
    }
}
