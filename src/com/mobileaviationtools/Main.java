package com.mobileaviationtools;

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

    public static void main(String[] args) {
	// write your code here
        AirspaceDataSource testSource = new AirspaceSQLITEDataSource();
        testSource.Open();
        testSource.createTables();

        Airspaces airspaces = new Airspaces();
        airspaces.OpenOpenAirTextFile("C:\\downloads\\openaip\\EHv16_3.txt", "NL");
        airspaces.OpenOpenAirTextFile("C:\\downloads\\openaip\\BELLUX_WEEK_140501.txt", "BE");
        airspaces.OpenOpenAirTextFile("C:\\downloads\\openaip\\Germany_Week13_2016.txt", "DE");
        airspaces.insertIntoDatabase(null, AirspaceDBHelper.AIRSPACES_TABLE_NAME, DatabaseType.SQLITE);


        //System.out.println("Download XSoar Files");
        //downloadXsourFiles();
        //System.out.println("XSoar files downloaded");

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

        airspaces.insertIntoDatabase(null, OPENAIR_TABLE_NAME, DatabaseType.POSTGRESQL);
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
