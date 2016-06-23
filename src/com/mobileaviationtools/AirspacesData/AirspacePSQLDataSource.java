package com.mobileaviationtools.AirspacesData;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.vividsolutions.jts.io.WKTWriter;

import java.sql.*;

/**
 * Created by Rob Verhoef on 20-10-2015.
 */
public class AirspacePSQLDataSource implements AirspaceDataSource {
    public AirspacePSQLDataSource()
    {

    }

    private Connection conn;

    public void Open(String databaseName)
    {
        Open();
    }

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

    public Boolean checkAirspace(Airspace airspace, String tablename)
    {
        Boolean ret = false;
        String q = "SELECT id FROM " + tablename + " WHERE airspace_id>0 AND airspace_id=?";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(q);
            pst.setLong(1, airspace.ID);
            ResultSet set = pst.executeQuery();
            ret = set.next();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void insertAirspace(Airspace airspace, String tablename)
    {
        if (checkAirspace(airspace, tablename)) return;

        String q = "INSERT INTO " + tablename + " (name, version, "
                + "category, airspace_id, country, altLimit_top, altlimit_top_unit, "
                + "altlimit_top_ref, altlimit_bottom, altlimit_bottom_unit, "
                + "altlimit_bottom_ref, geometry) VALUES (?,?,?,?,?,?,?,?,?,?,?, ST_GeomFromText(?))";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(q);
            pst.setString(1, airspace.Name);
            pst.setString(2, airspace.Version);
            pst.setString(3, airspace.Category.toString());
            pst.setLong(4, airspace.ID);
            pst.setString(5, airspace.Country);
            pst.setLong(6, airspace.getAltLimit_Top());
            pst.setString(7, airspace.AltLimit_Top_Unit.toString());
            pst.setString(8, airspace.AltLimit_Top_Ref.toString());
            pst.setLong(9, airspace.getAltLimit_Bottom());
            pst.setString(10, airspace.AltLimit_Bottom_Unit.toString());
            pst.setString(11, airspace.AltLimit_Bottom_Ref.toString());
            pst.setString(12, new WKTWriter().write(airspace.getGeometry()));

            pst.executeUpdate();
            pst.close();

            System.out.println("Inserted Airspace in database: " + airspace.Name);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertFir(Airspace airspace)
    {
        String q = "INSERT INTO tbl_firs (name, version, "
                + "category, airspace_id, country, altLimit_top, altlimit_top_unit, "
                + "altlimit_top_ref, altlimit_bottom, altlimit_bottom_unit, "
                + "altlimit_bottom_ref, geometry) VALUES (?,?,?,?,?,?,?,?,?,?,?, ST_GeomFromText(?))";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(q);
            pst.setString(1, airspace.Name);
            pst.setString(2, airspace.Version);
            pst.setString(3, "FIR");
            pst.setLong(4, airspace.ID);
            pst.setString(5, airspace.Country);
            pst.setLong(6, airspace.AltLimit_Top);
            pst.setString(7, airspace.AltLimit_Top_Unit.toString());
            pst.setString(8, airspace.AltLimit_Top_Ref.toString());
            pst.setLong(9, airspace.AltLimit_Bottom);
            pst.setString(10, airspace.AltLimit_Bottom_Unit.toString());
            pst.setString(11, airspace.AltLimit_Bottom_Ref.toString());
            pst.setString(12, new WKTWriter().write(airspace.getGeometry()));

            pst.executeUpdate();
            pst.close();

            System.out.println("Inserted Airspace in database: " + airspace.Name);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createTables() {

    }
}
//CREATE TABLE tbl_airspaces
//        (
//                id SERIAL NOT NULL,
//                name text,
//                version text,
//                category text,
//                airspace_id bigint,
//                country text,
//                altlimit_top bigint,
//                altlimit_top_unit text,
//                altlimit_top_ref text,
//                altlimit_bottom bigint,
//                altlimit_bottom_unit text,
//                altlimit_bottom_ref text,
//                geometry geometry,
//                CONSTRAINT airspaces_pkey PRIMARY KEY (id)
//)
//        WITH (
//        OIDS=FALSE
//        );
//        ALTER TABLE tbl_airspaces
//        OWNER TO postgres;

//CREATE TABLE tbl_firs
//        (
//                id SERIAL NOT NULL,
//                name text,
//                version text,
//                category text,
//                airspace_id bigint,
//                country text,
//                altlimit_top bigint,
//                altlimit_top_unit text,
//                altlimit_top_ref text,
//                altlimit_bottom bigint,
//                altlimit_bottom_unit text,
//                altlimit_bottom_ref text,
//                geometry geometry,
//                CONSTRAINT firs_pkey PRIMARY KEY (id)
//)
//        WITH (
//        OIDS=FALSE
//        );
//        ALTER TABLE tbl_firs
//        OWNER TO postgres;