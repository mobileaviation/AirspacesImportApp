package com.mobileaviationtools.AirspacesData;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Rob Verhoef on 20-10-2015.
 */
public class Airspace {
    public Airspace()
    {
        coordinates = new ArrayList<Coordinate>();
        AltLimit_Top = 0;
        AltLimit_Top_Ref = AltitudeReference.STD;
        AltLimit_Top_Unit = AltitudeUnit.F;
        AltLimit_Bottom = 0;
        AltLimit_Bottom_Ref = AltitudeReference.STD;
        AltLimit_Bottom_Unit = AltitudeUnit.F;
    }

    public AirspaceCategory Category;
    public String Version;
    public Integer ID;
    public String Country;
    public String Name;
    public Integer AltLimit_Top;
    public Integer getAltLimit_Top()
    {
        return (AltLimit_Top == null) ? 0 : AltLimit_Top;
    }
    public AltitudeUnit AltLimit_Top_Unit;

    public AltitudeReference AltLimit_Top_Ref;
    public Integer AltLimit_Bottom;
    public Integer getAltLimit_Bottom()
    {
        return (AltLimit_Top == null) ? 0 : AltLimit_Bottom;
    }
    public AltitudeUnit AltLimit_Bottom_Unit;
    public AltitudeReference AltLimit_Bottom_Ref;
    private com.vividsolutions.jts.geom.Geometry Geometry;
    private com.vividsolutions.jts.geom.LineString Line;
    public ArrayList<Coordinate> coordinates;

    public Geometry getGeometry()
    {
        if (Geometry == null) {
            if (coordinates.size()==0)
            {
                int i=1;
                return null;
            }
            if ((coordinates.get(0).x != coordinates.get(coordinates.size()-1).x) ||
                    (coordinates.get(0).y != coordinates.get(coordinates.size()-1).y) )
            {
                coordinates.add(coordinates.get(0));
            }
            Coordinate[] c = coordinates.toArray(new Coordinate[coordinates.size()]);

            try {
                Geometry = new GeometryFactory().createPolygon(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Geometry;
    }

    public LineString getLine()
    {
        if (Line == null)
        {
            Coordinate[] c = coordinates.toArray(new Coordinate[coordinates.size()]);

            try {
                Line = new GeometryFactory().createLineString(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Line;
    }

    public Geometry getEnvelope()
    {
        if (Geometry == null) {
            Coordinate[] c = coordinates.toArray(new Coordinate[coordinates.size()]);
            Geometry = new GeometryFactory().createPolygon(c);
        }
        return Geometry.getEnvelope();
    }

    public void setGeometry(Geometry geometry)
    {
        this.Geometry = geometry;
        coordinates = new ArrayList<>(Arrays.asList(geometry.getCoordinates()));
    }
}
