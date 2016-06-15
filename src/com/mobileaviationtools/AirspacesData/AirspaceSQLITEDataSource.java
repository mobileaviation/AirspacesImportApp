package com.mobileaviationtools.AirspacesData;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Rob Verhoef on 13-6-2016.
 */
public class AirspaceSQLITEDataSource implements AirspaceDataSource {
    private Connection conn;

    public void Open() {
        try {
            conn = null;
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:C:/Downloads/openaip/airspaces.db.sqlite");

            System.out.println("Connection to C:/Downloads/openaip/airspaces.db.sqlite is open");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean checkAirspace(Airspace airspace, String tablename) {
        return null;
    }

    public void insertAirspace(Airspace airspace, String tablename) {
        String q = "INSERT INTO " + tablename + " (name, version, "
                + "category, airspace_id, country, altLimit_top, altlimit_top_unit, "
                + "altlimit_top_ref, altlimit_bottom, altlimit_bottom_unit, "
                + "altlimit_bottom_ref, geometry, envelope) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(q);
            pst.setString(1, airspace.Name);
            pst.setString(2, airspace.Version);
            pst.setString(3, airspace.Category.toString());
            pst.setLong(4, airspace.ID);
            pst.setString(5, airspace.Country);
            pst.setLong(6, airspace.AltLimit_Top);
            pst.setString(7, airspace.AltLimit_Top_Unit.toString());
            pst.setString(8, airspace.AltLimit_Top_Ref.toString());
            pst.setLong(9, airspace.AltLimit_Bottom);
            pst.setString(10, airspace.AltLimit_Bottom_Unit.toString());
            pst.setString(11, airspace.AltLimit_Bottom_Ref.toString());
//            byte[] geometry = new WKBWriter().write(airspace.getGeometry());
//            Geometry g1;// = airspace.getGeometry();
//            String gis;
//            try {
//                g1  = new WKBReader().read(geometry);
//                gis = new WKTWriter().write(g1);
//                int i = 0;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            //pst.setString(12, new WKTWriter().write(airspace.getGeometry()));
            pst.setBytes(12, new WKBWriter().write(airspace.getGeometry()));
            //String evelope = new WKTWriter().write(airspace.getEnvelope());
            pst.setBytes(13, new WKBWriter().write(airspace.getEnvelope()));

            pst.executeUpdate();
            pst.close();

            System.out.println("Inserted Airspace in database: " + airspace.Name);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertFir(Airspace airspace) {

    }

    public void createTables()
    {
        AirspaceDBHelper.createTables(conn);
    }
}
