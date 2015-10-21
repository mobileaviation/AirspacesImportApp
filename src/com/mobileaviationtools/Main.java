package com.mobileaviationtools;

import com.mobileaviationtools.AirspacesData.Airspaces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
	// write your code here
        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/airnav";
            Connection conn = DriverManager.getConnection(url, "postgres", "ko218493");

            System.out.println("Connected to database");

            Airspaces airspaces = new Airspaces();
            //airspaces.OpenAipFile("openaip_airspace_netherlands_nl.aip");

            airspaces.OpenOpenAirTextFile("EHv15_3c.txt");

            conn.close();

            System.out.println("Disconnected!");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
