package com.mobileaviationtools;

import com.mobileaviationtools.AirspacesData.Airspace;
import com.mobileaviationtools.AirspacesData.AirspaceCategory;
import com.mobileaviationtools.AirspacesData.Airspaces;
import com.mobileaviationtools.Links.Link;
import com.mobileaviationtools.Links.LinksDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    private static String OPENAIP_TABLE_NAME = "tbl_openaip_airspaces";
    private static String OPENAIR_TABLE_NAME = "tbl_openair_airspaces";

    public static void main(String[] args) {
	// write your code here
        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/airnav";
            Connection conn = DriverManager.getConnection(url, "postgres", "ko218493");

            System.out.println("Connected to database");

            //Airspaces airspaces = new Airspaces();
            //airspaces.OpenOpenAirTextFile("C:\\downloads\\openaip\\IrelandSua2014.txt", "UK");
            //airspaces.insertIntoDatabase(null);


            ArrayList<Link> links = downloadXsourFiles();
            readFIRFromXsoarFiles(links);

            //ArrayList<Link> links = getOpenaipFiles();
            //readOpenaipFiles(links);

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
        airspaces.OpenAipFile("C:\\Downloads\\openaip\\openaip_airspace_netherlands_nl.aip", OPENAIP_TABLE_NAME);
        airspaces.OpenAipFile("C:\\Downloads\\openaip\\openaip_airspace_germany_de.aip", OPENAIP_TABLE_NAME);
        airspaces.OpenAipFile("C:\\Downloads\\openaip\\openaip_airspace_belgium_be.aip", OPENAIP_TABLE_NAME);
    }

    private static ArrayList<Link> getOpenaipFiles()
    {
        LinksDataSource linksDataSource = new LinksDataSource();
        linksDataSource.Open();
        linksDataSource.getOpenaipFilesFromDB();
        linksDataSource.Close();

        return linksDataSource.links;
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

        airspaces.insertIntoDatabase(null, OPENAIR_TABLE_NAME);
    }

    private static void readOpenaipFiles(ArrayList<Link> links)
    {
        Airspaces airspaces = new Airspaces();
        for (Link link : links)
        {
            if (new File(link.getLocalFile()).exists()) {
                System.out.println(link.getLocalFile() + " Found");
                airspaces.OpenAipFile(link.getLocalFile(), OPENAIP_TABLE_NAME);
            }
            else
                System.out.println(link.getLocalFile() + " NOT Found");
        }
    }
}
