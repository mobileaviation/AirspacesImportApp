package com.mobileaviationtools.AirspacesData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Rob Verhoef on 20-10-2015.
 */
public class AirspaceDataSource {
    public AirspaceDataSource()
    {

    }

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

    public void insertAirspace(Airspace airspace)
    {
        String q = "INSERT INTO tbl_Airspaces (name, version, "
                + "category, id, country, altLimit_top, altLimit_top_unit, "
                + "altLimit_top_ref, altLimit_bottom, altLimit_bottom_unit, "
                + "altLimit_bottom_ref) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
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

            pst.executeUpdate();
            pst.close();

            System.out.println("Inserted Airspace in database: " + airspace.Name);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
//"_ID" bigint,
// 1       name text,
// 2       version text,
// 3       category text,
// 4       id bigint,
// 5       country text,
// 6       "altLimit_top" bigint,
// 7       "altLimit_top_unit" text,
// 8       "altLimit_top_ref" text,
// 9       "altLimit_bottom" bigint,
// 10       "altLimit_bottom_unit" bigint,
// 11       "altLimit_bottom_ref" bigint,
// 12       geometry geometry