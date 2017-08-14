package com.mobileaviationtools.AirspacesData;


import com.vividsolutions.jts.geom.Coordinate;

import javax.json.*;
import java.io.*;

/**
 * Created by Rob Verhoef on 7-8-2017.
 */
public class AirspaceGeoJsonSource {

    public AirspaceGeoJsonSource(String CountryCode, String Path)
    {
        countryCode = CountryCode;
        filePath = Path + "\\" + countryCode + "_airspaces.json";
    }

    private String countryCode;
    private String filePath;
    private String json;
    private JsonObject airspaceJsonObject;


    public void InsertAirspaces(Airspaces airspaces)
    {
        JsonObjectBuilder airspaceJsonObjectbuilder = Json.createObjectBuilder().add("type","FeatureCollection");
        JsonArrayBuilder airspacesArray = Json.createArrayBuilder();

        for(Airspace airspace: airspaces)
        {
            JsonObjectBuilder feature = Json.createObjectBuilder().add("type", "Feature");
            JsonObjectBuilder properties = Json.createObjectBuilder()
                    .add("stoke", "#000000")
                    .add("stroke-width", 1)
                    .add("stroke-opacity", 1)
                    .add("fill", "#000000")
                    .add("fill-opacity", 0)
                    .add("airspace-name", (airspace.Name == null)? "UNK" : airspace.Name )
                    .add("airspace-category", (airspace.Category == null)? "UNK" : airspace.Category.toString());

            feature.add("properties", properties.build());

            JsonArrayBuilder polygons = Json.createArrayBuilder();
            JsonArrayBuilder coordinates = Json.createArrayBuilder();
            for (Coordinate c: airspace.coordinates)
            {
                JsonArrayBuilder coordinate = Json.createArrayBuilder().add(c.x).add(c.y);
                coordinates.add(coordinate);
            }

            polygons.add(coordinates);

            JsonObjectBuilder geometry = Json.createObjectBuilder().add("type", "Polygon")
                    .add("coordinates", polygons);

            feature.add("geometry",geometry.build());

            airspacesArray.add(feature.build());

        }

        airspaceJsonObject = airspaceJsonObjectbuilder.add("features", airspacesArray).build();

        json = airspaceJsonObject.toString();
    }

    public void SaveGeoJsonFile()
    {
        try {
            File JsonFile = new File(filePath);

            FileWriter fw = new FileWriter(JsonFile);
            fw.write(json);
            fw.close();

        } catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        }
    }

    public void SaveGeoJsonObjectToFile()
    {
        //write to file
        OutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
            JsonWriter jsonWriter = Json.createWriter(os);
            jsonWriter.writeObject(airspaceJsonObject);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /**
         * We can get JsonWriter from JsonWriterFactory also
         JsonWriterFactory factory = Json.createWriterFactory(null);
         jsonWriter = factory.createWriter(os);
         */

    }
}
