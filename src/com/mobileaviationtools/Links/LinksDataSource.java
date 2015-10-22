package com.mobileaviationtools.Links;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Rob Verhoef on 21-10-2015.
 */
public class LinksDataSource {

    public LinksDataSource()
    {
        links = new ArrayList<>();
    }

    public ArrayList<Link> links;

    private Connection conn;

    public void Open()
    {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/airnav";
            conn = DriverManager.getConnection(url, "postgres", "ko218493");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Close()
    {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void downloadXsoarFiles(Boolean override)
    {
        String q = "SELECT * FROM tbl_links WHERE enabled=TRUE;";

        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(q);
            ResultSet set = pst.executeQuery();

            while (set.next())
            {
                Link l = new Link("C:\\Downloads\\openaip\\");
                l.readResultSet(set);
                links.add(l);

                l.downloadFile(override);

                System.out.println("Loaded: " + l.getXsoarLink());
            }
        }
        catch (SQLException e) {
        e.printStackTrace();
        }
    }

    public void downloadTest()
    {
        try {
            FileUtils.copyURLToFile(new URL("http://www.openaip.net/system/files/airspaces/openaip_airspace_belgium_be.aip_1444791895"),
                    new File("C:\\Downloads\\openaip\\openaip_airspace_belgium_be.aip") );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
//CREATE TABLE tbl_links
//        (
//                id SERIAL NOT NULL,
//                country text,
//                countrycode text,
//                openaiplink text,
//                xsoarlink text,
//                enabled boolean NOT NULL DEFAULT true,
//                CONSTRAINT links_pkey PRIMARY KEY (id)
//)
//        WITH (
//        OIDS=FALSE
//        );
//        ALTER TABLE tbl_links
//        OWNER TO postgres;