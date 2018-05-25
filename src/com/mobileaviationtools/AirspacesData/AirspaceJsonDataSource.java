package com.mobileaviationtools.AirspacesData;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.io.WKTWriter;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.*;

public class AirspaceJsonDataSource {
    public AirspaceJsonDataSource(String jsonFileName)
    {
        this.jsonFilename = jsonFileName;
        index = 0;
    }

    private String jsonFilename;
    private JsonGenerator jsonGenerator;
    private JsonObjectBuilder airspaceJsonObjectBuilder;
    private JsonObjectBuilder indexObjectBuilder;
    private Integer index;

    public void Open()
    {
        try {
            //fos = new FileOutputStream(jsonFilename);
            //jsonGenerator = Json.createGenerator(fos);
            //jsonGenerator.writeStartObject("airspaces");

            airspaceJsonObjectBuilder = Json.createObjectBuilder();
            indexObjectBuilder = Json.createObjectBuilder();
        }
        catch (Exception ee)
        {
            System.out.println("Json create error: " + ee.getMessage());
        }
    }

    private void SaveGeoJsonFile(JsonObject json)
    {
        try {
            File JsonFile = new File(this.jsonFilename);

            FileWriter fw = new FileWriter(JsonFile);
            fw.write(json.toString());
            fw.close();

        } catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        }
    }

    public void Close()
    {
        airspaceJsonObjectBuilder.add("airspaces", indexObjectBuilder.build());
        JsonObject airspacesObj = airspaceJsonObjectBuilder.build();
        SaveGeoJsonFile(airspacesObj);
//        jsonGenerator.writeEnd();
//        jsonGenerator.close();
    }

    public void AddAirspaces(Airspaces airspaces)
    {
        for (Airspace a: airspaces)
        {
            try {
                if (a.getGeometry() != null) {
                    JsonObject airspaceObj = AddAirspace(a, index);
                    indexObjectBuilder.add(index.toString(), airspaceObj);
                    index++;
                }
            }
            catch (Exception ee) {
                System.out.println("Add Airspace Error: " + ee.getMessage());
            }
        }
    }

//    public void InsertAirspaces(Airspaces airspaces)
//    {
//        JsonObjectBuilder airspaceJsonObjectbuilder = Json.createObjectBuilder().add("type","FeatureCollection");
//        JsonArrayBuilder airspacesArray = Json.createArrayBuilder();
//
//        for(Airspace airspace: airspaces)
//        {
//            JsonObjectBuilder feature = Json.createObjectBuilder().add("type", "Feature");
//            JsonObjectBuilder properties = Json.createObjectBuilder()
//                    .add("stoke", "#000000")
//                    .add("stroke-width", 1)
//                    .add("stroke-opacity", 1)
//                    .add("fill", "#000000")
//                    .add("fill-opacity", 0)
//                    .add("airspace-name", (airspace.Name == null)? "UNK" : airspace.Name )
//                    .add("airspace-category", (airspace.Category == null)? "UNK" : airspace.Category.toString());
//
//            feature.add("properties", properties.build());
//
//            JsonArrayBuilder polygons = Json.createArrayBuilder();
//            JsonArrayBuilder coordinates = Json.createArrayBuilder();
//            for (Coordinate c: airspace.coordinates)
//            {
//                JsonArrayBuilder coordinate = Json.createArrayBuilder().add(c.x).add(c.y);
//                coordinates.add(coordinate);
//            }
//
//            polygons.add(coordinates);
//
//            JsonObjectBuilder geometry = Json.createObjectBuilder().add("type", "Polygon")
//                    .add("coordinates", polygons);
//
//            feature.add("geometry",geometry.build());
//
//            airspacesArray.add(feature.build());
//
//        }
//
//        airspaceJsonObject = airspaceJsonObjectbuilder.add("features", airspacesArray).build();
//
//        json = airspaceJsonObject.toString();
//    }


    private JsonObject AddAirspace(Airspace airspace, Integer index)
    {
        //jsonGenerator.writeStartObject(index.toString());
//        jsonGenerator.writeStartObject();

        JsonObjectBuilder airspaceObject = Json.createObjectBuilder();

        airspaceObject.add("index", index);
        airspaceObject.add("ID", airspace.ID);
        airspaceObject.add("Name", airspace.Name);
        airspaceObject.add("Country", airspace.Country);
        airspaceObject.add("Version", airspace.Version);
        airspaceObject.add("Category", airspace.Category.toString());
        airspaceObject.add("AltLimit_Top", airspace.AltLimit_Top);
        airspaceObject.add("AltLimit_Top_Ref", airspace.AltLimit_Top_Ref.toString());
        airspaceObject.add("AltLimit_Top_Unit", airspace.AltLimit_Top_Unit.toString());
        airspaceObject.add("AltLimit_Bottom", airspace.AltLimit_Bottom);
        airspaceObject.add("AltLimit_Bottom_Ref", airspace.AltLimit_Bottom_Ref.toString());
        airspaceObject.add("AltLimit_Bottom_Unit", airspace.AltLimit_Bottom_Unit.toString());

        String coordinates = new WKTWriter().write(airspace.getGeometry());
        airspaceObject.add("Coordinates", coordinates);

        return airspaceObject.build();

//        jsonGenerator.writeStartArray("coordinates");
//        for (Coordinate c: airspace.coordinates)
//        {
//            jsonGenerator.write("x", c.x);
//            jsonGenerator.write("y", c.y);
//        }
//        jsonGenerator.writeEnd();
//
//        jsonGenerator.writeEnd();

    }


}
