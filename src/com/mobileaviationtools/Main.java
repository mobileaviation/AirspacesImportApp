package com.mobileaviationtools;

import com.mobileaviationtools.AirspacesData.AirspaceCategory;
import com.mobileaviationtools.AirspacesData.Airspaces;
import com.mobileaviationtools.Links.Link;
import com.mobileaviationtools.Links.LinksDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	// write your code here
        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/airnav";
            Connection conn = DriverManager.getConnection(url, "postgres", "ko218493");

            System.out.println("Connected to database");

            //Airspaces airspaces = new Airspaces();
            //airspaces.OpenOpenAirTextFile("C:\\downloads\\openaip\\test.txt", "Switserland");


            ArrayList<Link> links = downloadXsourFiles();
            readFIRFromXsoarFiles(links);

//            readOpenaipTestFiles();

            conn.close();

            System.out.println("Disconnected!");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void readOpenaipTestFiles()
    {
        Airspaces airspaces = new Airspaces();
        airspaces.OpenAipFile("C:\\Downloads\\openaip\\openaip_airspace_netherlands_nl.aip");
        airspaces.OpenAipFile("C:\\Downloads\\openaip\\openaip_airspace_germany_de.aip");
        airspaces.OpenAipFile("C:\\Downloads\\openaip\\openaip_airspace_belgium_be.aip");
    }

    private static ArrayList<Link> downloadXsourFiles()
    {
        LinksDataSource linksDataSource = new LinksDataSource();
        linksDataSource.Open();
        linksDataSource.downloadXsoarFiles(false);
        linksDataSource.Close();

        return linksDataSource.links;
    }

    private static void readFIRFromXsoarFiles(ArrayList<Link> links)
    {
        Airspaces airspaces = new Airspaces();
        for (Link link : links)
        {
            airspaces.OpenOpenAirTextFile(link.getLocalFile(), link.country);
        }

        airspaces.insertIntoDatabase(null);
    }
}
