package com.mobileaviationtools;

import com.mobileaviationtools.AirportData.Airports;
import com.mobileaviationtools.AirspacesData.*;
import com.mobileaviationtools.Links.Link;
import com.mobileaviationtools.Links.LinksDataSource;
import com.mobileaviationtools.OpenStreetMap.osm;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    private static String OPENAIP_TABLE_NAME = "tbl_openaip_airspaces";
    private static String OPENAIR_TABLE_NAME = "tbl_openair_airspaces";
    private static String PATH = "C:\\AirnavData\\Airspaces\\";


    public static void main(String[] args) {
	// write your code here

        /* Dit is code voor het lezen van de airports CSV en polygons in postgresql
        */
        //Airports airports = new Airports();
        //airports.importCSV("C:\\Downloads\\ourairports\\airports.csv");

        System.out.println("Download XSoar Files");
        ArrayList<Link> links = downloadXsourFiles("airspaces_info.sqlite");
        System.out.println("XSoar files downloaded");

        /*  Dit is code voor het maken van de airspace mapping naar Postgresql
        */
        //AirspaceDataSource testSource = new AirspaceSQLITEDataSource();
        //AirspaceDataSource testSource = new AirspacePSQLDataSource();
        //String databaseName = "NL_airspaces.db.sqlite";
        //testSource.Open(databaseName);
        //testSource.createTables();
        //Airspaces airspacesJson = new Airspaces();
        //airspacesJson.OpenOpenAirTextFile("C:\\downloads\\xSoar\\EHv17_3.txt", "NL");

        //AirspaceGeoJsonSource airspaceGeoJsonSource = new AirspaceGeoJsonSource("NL", "C:\\downloads\\xSoar\\");
        //airspaceGeoJsonSource.InsertAirspaces(airspacesJson);
        //airspaceGeoJsonSource.SaveGeoJsonObjectToFile();

        //airspaces.insertIntoDatabase(null, AirspaceDBHelper.AIRSPACES_TABLE_NAME, DatabaseType.SQLITE, databaseName);
        //testSource.Close();




//        AirspaceDataSource testSource = new AirspaceSQLITEDataSource();
//        String databaseName = "all_airspaces.db.sqlite";
//        testSource.Open(databaseName);
//        testSource.createTables();

        AirspaceJsonDataSource airspaceJsonDataSource = new AirspaceJsonDataSource(PATH + "airspaces.json");
        airspaceJsonDataSource.Open();

        for (Link link : links) {

            Airspaces airspaces = new Airspaces();
            airspaces.OpenOpenAirTextFile(link.getLocalFile(), link.countryCode);

            //airspaces.insertIntoDatabase(null, AirspaceDBHelper.AIRSPACES_TABLE_NAME, DatabaseType.SQLITE, databaseName);

            //AirspaceGeoJsonSource airspaceGeoJsonSource = new AirspaceGeoJsonSource(link.countryCode, "C:\\downloads\\xSoar\\");
            //airspaceGeoJsonSource.InsertAirspaces(airspaces);
            //airspaceGeoJsonSource.SaveGeoJsonObjectToFile();

            //airspaces.insertIntoDatabase(null, OPENAIR_TABLE_NAME, DatabaseType.POSTGRESQL, databaseName);

            airspaceJsonDataSource.AddAirspaces(airspaces);
            //airspaceJsonDataSource.Close();

            System.out.println("Inserted airspace");

        }

        airspaceJsonDataSource.Close();

//        testSource.Close();

        //try {
            //Class.forName("org.postgresql.Driver");

            //String url = "jdbc:postgresql://localhost:5432/airnav";
            //Connection conn = DriverManager.getConnection(url, "postgres", "ko218493");

            //System.out.println("Connected to database");

            //Airspaces airspaces = new Airspaces();
            //airspaces.OpenOpenAirTextFile("C:\\downloads\\openaip\\IrelandSua2014.txt", "UK");
            //airspaces.insertIntoDatabase(null);


            //ArrayList<Link> links = downloadXsourFiles();
            //readFIRFromXsoarFiles(links);

            //ArrayList<Link> links = getOpenaipFiles();
            //readOpenaipFiles(links);

//            readOpenaipTestFiles();

            //conn.close();

          //  System.out.println("Disconnected!");

//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        System.out.println("OSM Export test");
//        Airspaces airspaces = new Airspaces();
//        airspaces.OpenAipFile("C:\\Downloads\\openaip\\openaip_airspace_netherlands_nl.aip", OPENAIP_TABLE_NAME);
//
//        try {
//
//            osm _osm = new osm();
//            _osm.createAirspaceOSM(airspaces);
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            DOMSource source = new DOMSource(_osm.document);
//            StreamResult result = new StreamResult(new File("C:\\test\\osm.xml"));
//            transformer.transform(source, result);
//
//            System.out.println("Dosument created");
//
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
//        catch (TransformerException te)
//        {
//            te.printStackTrace();
//        }

    }

    private static void readOpenaipTestFiles()
    {
        Airspaces airspaces = new Airspaces();
        airspaces.OpenAipFile("C:\\Downloads\\openaip\\openaip_airspace_netherlands_nl.aip", OPENAIP_TABLE_NAME);
        //airspaces.OpenAipFile("C:\\Downloads\\openaip\\openaip_airspace_germany_de.aip", OPENAIP_TABLE_NAME);
        //airspaces.OpenAipFile("C:\\Downloads\\openaip\\openaip_airspace_belgium_be.aip", OPENAIP_TABLE_NAME);
    }

    private static ArrayList<Link> getOpenaipFiles()
    {
        LinksDataSource linksDataSource = new LinksDataSource();
        linksDataSource.Open();
        linksDataSource.getOpenaipFilesFromDB();
        linksDataSource.Close();

        return linksDataSource.links;
    }

    private static ArrayList<Link> downloadXsourFiles(String databasename)
    {
        LinksDataSource linksDataSource = new LinksDataSource();
        linksDataSource.OpenSQLite(databasename);
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

        airspaces.insertIntoDatabase(null, OPENAIR_TABLE_NAME, DatabaseType.POSTGRESQL, "");
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
