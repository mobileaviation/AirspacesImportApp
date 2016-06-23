package com.mobileaviationtools.AirspacesData;

import java.sql.Connection;

/**
 * Created by Rob Verhoef on 13-6-2016.
 */
public interface AirspaceDataSource {
    public void Open();
    public void Open(String databaseName);
    public void Close();
    public Boolean checkAirspace(Airspace airspace, String tablename);
    public void insertAirspace(Airspace airspace, String tablename);
    public void insertFir(Airspace airspace);
    public void createTables();
}
