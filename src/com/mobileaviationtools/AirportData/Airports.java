package com.mobileaviationtools.AirportData;

import com.mobileaviationtools.Helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rob Verhoef on 26-7-2016.
 */
public class Airports extends ArrayList<Airport> {
    public Airports()
    {
        super();
    }

    public Airport airport;

    public void importCSV(String Filename)
    {
        try {
            String csv = Helpers.readFromFile(Filename);
            String[] lines = csv.split("[\\r\\n]+");


            for (int i = 1; i<lines.length; i++)
            {
                try {
                    airport = new Airport();

                    //String[] values = lines[i].split(",");
                    String[] values = new String[18];
                    String[] commas = lines[i].split("(\\\"[^\\\"]*\\\")|([0-9.-])+");
                    Pattern p = Pattern.compile("(\\\"[^\\\"]*\\\")|([0-9.-])+");
                    Matcher m = p.matcher(lines[i]);

                    Integer index = 0;
                    Integer outIndex = 0;
                    while(m.find()) {
                        try {
                            outIndex = outIndex + commas[index].length();
                            values[outIndex] = m.group();
                            index++;
                        }
                        catch (Exception e)
                        {
                            System.out.println("Error in CSV: " + airport.name);
                        }
                    }

                    //String[] values = lines[i].

                    airport.id = Integer.getInteger(Value(values, 0));
                    airport.ident = Value(values, 1);
                    airport.type = Value(values, 2);
                    airport.name = Value(values, 3);
                    airport.latitude_deg = Float.parseFloat(Value(values, 4));
                    airport.longitude_deg = Float.parseFloat(Value(values, 5));
                    try {
                        airport.elevation_ft = Integer.parseInt(Value(values, 6));
                    }
                    catch (Exception ee)
                    {
                        airport.elevation_ft = 0;
                    }
                    airport.continent = Value(values, 7);
                    airport.iso_country = Value(values, 8);
                    airport.iso_region = Value(values, 9);
                    airport.municipality = Value(values, 10);
                    airport.scheduled_service = Value(values, 11);
                    airport.gps_code = Value(values, 12);
                    airport.iata_code = Value(values, 13);
                    airport.local_code = Value(values, 14);
                    airport.home_link = Value(values, 15);
                    airport.wikipedia_link = Value(values, 16);
                    airport.keywords = Value(values, 17);

                    System.out.println("Airport: " + airport.name);
                    this.add(airport);
                }
                catch (Exception e)
                {
                    System.out.println("Error in CSV: " + airport.name);
                }


            }

            String firstline = lines[0];
        }
        catch (Exception ee)
        {
            System.out.println("Error" + airport.name);
        }

    }

    private String Value(String[] values, Integer index)
    {
        if (index<values.length)
            return values[index];
        else return "";

    }

}
