package com.mobileaviationtools.Links;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Rob Verhoef on 21-10-2015.
 */
public class Link {
    public Link(String localPath)
    {
        this.localPath = localPath;
    }

    private String localPath;

    public Integer ID;
    public Integer openaip_Id;
    private String localFile;
    public String getLocalFile()
    {
        return localFile;
    }
    private String xsoarLink;
    public void setXsoarLink(String xsoarLink)
    {
        this.xsoarLink = xsoarLink;
        downloadale = (xsoarLink!=null);

        if (downloadale)
        {
            String s[] = xsoarLink.split("/");
            localFile = localPath + s[s.length-1];
        }

    }
    public String getXsoarLink()
    {
        return enabled ? xsoarLink : openaipLink;
    }
    private String openaipLink;
    public void setOpenaipLink(String openaipLink)
    {
        this.openaipLink = openaipLink;// + "_" + openaip_Id.toString();
        downloadale = (openaipLink!=null);

        if (downloadale)
        {
            String s[] = openaipLink.split("/");
            localFile = localPath + s[s.length-1];
        }
    }
    public String getOpenaipLink()
    {
        return openaipLink;
    }

    public String country;
    public String countryCode;

    private Boolean downloadale;
    public Boolean enabled;
    public Boolean openaip_enabled;

    public void readResultSet(ResultSet resultSet)
    {
        try {
            ID = resultSet.getInt("id");
            enabled = resultSet.getBoolean("enabled");
            openaip_enabled = resultSet.getBoolean("openaip_enabled");
            openaip_Id = resultSet.getInt("openaip_id");

            if (enabled) setXsoarLink(resultSet.getString("xsoarlink"));
            else xsoarLink = resultSet.getString("xsoarlink");
            if (openaip_enabled) setOpenaipLink(resultSet.getString("openaiplink"));
                else openaipLink = resultSet.getString("openaiplink");
            country = resultSet.getString("country");
            countryCode = resultSet.getString("countrycode");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(Boolean override)
    {
        String l = "";
        if (enabled) l = xsoarLink;
        if (openaip_enabled) l = openaipLink;
        if (downloadale)
            if ((new File(localFile).exists())) {
                if (override) p_downloadFile(l);
            }
            else
            {
                p_downloadFile(l);
            }
    }

    private void p_downloadFile(String _link)
    {
        try {
            FileUtils.copyURLToFile(new URL(_link), new File(localFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
