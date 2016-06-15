package com.mobileaviationtools.AirspacesData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Rob Verhoef on 13-6-2016.
 */
public class AirspaceDBHelper {

    private static final String DATABASE_NAME = "airspaces.db";
    private static final int DATABASE_VERSION = 1;

    public static final String AIRSPACES_TABLE_NAME = "tbl_Airspaces";

    private static final String TAG = "GooglemapsTest";

    public static final String C_name = "name";
    public static final String C_version = "version";
    public static final String C_category = "category";
    public static final String C_airspace_id = "airspace_id";
    public static final String C_country = "country";
    public static final String C_altLimit_top = "altLimit_top";
    public static final String C_altLimit_top_unit = "altLimit_top_unit";
    public static final String C_altLimit_top_ref = "altLimit_top_ref";
    public static final String C_altLimit_bottom = "altLimit_bottom";
    public static final String C_altLimit_bottom_unit = "altLimit_bottom_unit";
    public static final String C_altLimit_bottom_ref = "altLimit_bottom_ref";
    public static final String C_geometry = "geometry";
    public static final String C_envelope = "envelope";

    public static final String AIRSPACES_TABLE = "create table " +
            AIRSPACES_TABLE_NAME + " (_id integer primary key autoincrement, " +
            C_name + " text, " +
            C_version + " text, " +
            C_category + " text, " +
            C_airspace_id + " text, " +
            C_country + " text, " +
            C_altLimit_top + " integer, " +
            C_altLimit_top_unit + " text, " +
            C_altLimit_top_ref + " text, " +
            C_altLimit_bottom + " integer, " +
            C_altLimit_bottom_unit + " text, " +
            C_altLimit_bottom_ref + " text, " +
            C_geometry + " blob, " +
            C_envelope + " blob );";

//    public static final String AIRSPACES_ENVELOPE_INDEX = "create index envelope_index " +
//            "on " + AIRSPACES_TABLE_NAME + " (" +
//            C_lat_top_left + "," + C_lon_top_left + "," + C_lat_bottom_right + "," + C_lot_bottom_right +
//            ");";

    public static void createTables(Connection connection)
    {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.execute(AIRSPACES_TABLE);
            System.out.println("Airspaces table created");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
