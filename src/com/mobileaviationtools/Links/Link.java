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
        return xsoarLink;
    }
    private String openaipLink;
    public void setOpenaipLink(String openaipLink)
    {
        this.openaipLink = openaipLink;
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

    public void readResultSet(ResultSet resultSet, Boolean openaip)
    {
        try {
            ID = resultSet.getInt("id");

            if (!openaip) setXsoarLink(resultSet.getString("xsourlink"));
            else xsoarLink = resultSet.getString("xsourlink");
            if (openaip) setOpenaipLink(resultSet.getString("openaiplink"));
                else openaipLink = resultSet.getString("openaiplink");
            country = resultSet.getString("country");
            countryCode = resultSet.getString("countrycode");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(Boolean override)
    {
        if (downloadale)
            if ((new File(localFile).exists())) {
                if (override) p_downloadFile();
            }
            else
            {
                p_downloadFile();
            }
    }

    private void p_downloadFile()
    {
        try {
            FileUtils.copyURLToFile(new URL(xsoarLink), new File(localFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
