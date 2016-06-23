package com.mobileaviationtools.AirspacesData;

import com.mobileaviationtools.Classes.LatLng;
import com.mobileaviationtools.Helpers;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rob Verhoef on 20-10-2015.
 */
public class Airspaces extends ArrayList<Airspace> {
    private String TAG = "GooglemapsTest";

    public Airspaces()
    {

    }


    public void Add(Airspace airspace)
    {
        this.add(airspace);
    }

    public void OpenAipFile(String filename, String tablename)
    {
        //String _filename = Environment.getExternalStorageDirectory().toString()+"/Download/" + filename;
        String _filename = filename;
        String XML = readFromFile( _filename);

        System.out.println("Read XML file: " + _filename);
        readOpenAipXML(XML);
        System.out.println("XML Read");



        //insertIntoDatabase(null, tablename);
        //Log.i(TAG, "AirspaceDataSource Insert Finished");
    }

    public void OpenOpenAirTextFile(String filename, String country)
    {
        String _filename = filename;
        String txt = readFromFile(_filename);
        readOpenAirText(txt, country);
    }

    public void insertIntoDatabase(AirspaceCategory category, String tablename, DatabaseType databaseType, String databaseName)
    {
        if (category != null) {
            if (category.equals(AirspaceCategory.FIR))
                p_insertIntoFir();
            else
                if (databaseType==DatabaseType.POSTGRESQL)
                    p_insertIntoDatabase(tablename);
                else i_insertIntoDatabase(databaseName,tablename);
        } else
        if (databaseType==DatabaseType.POSTGRESQL)
            p_insertIntoDatabase(tablename);
        else i_insertIntoDatabase(databaseName, tablename);
    }

    private void p_insertIntoFir()
    {
        AirspaceDataSource dataSource = new AirspacePSQLDataSource();
        dataSource.Open();

        for (Airspace airspace : this)
        {
            if (airspace.Category.equals(AirspaceCategory.FIR))
                dataSource.insertFir(airspace);
        }

        dataSource.Close();
    }

    private void p_insertIntoDatabase(String tablename)
    {
        AirspaceDataSource dataSource = new AirspacePSQLDataSource();
        dataSource.Open();

        for (Airspace airspace : this)
        {
            dataSource.insertAirspace(airspace, tablename);
        }

        dataSource.Close();
    }

    private void i_insertIntoDatabase(String databaseName, String tablename)
    {
        AirspaceDataSource dataSource = new AirspaceSQLITEDataSource();
        dataSource.Open(databaseName);

        for (Airspace airspace : this)
        {
            dataSource.insertAirspace(airspace, tablename);
        }

        dataSource.Close();
    }

    public String lines[];
    private void readOpenAirText(String text, String country)
    {
        try {
            //text = text.replace("\n", "\r\n");
            //lines = text.split("\r\n");
            lines = text.split("(?=\\bAC)|(?=\\bAN)|(?=\\bAH)|(?=\\bAL)|(?=\\bAF)|(?=\\bAG)|(?=\\bDP)|(?=\\bDB)|(?=\\bDC)|(?=\\bV\\sX)|(?=\\bV\\sD)");
            Airspace airspace = null;
            LatLng location = null;
            LatLng center = null;
            Boolean circle  = false;
            Boolean cw = true;
            //Boolean newAirspace = false;
            for (String l : lines)
            {

                if (!l.startsWith("*")) {
                    // Check is first char = * then discard this split[*]
                    // Read the first line for the Airspace Category (AC)
                    // Read the line with starts with AN, Following string is Name
                    // -- AH, unit (non if FT), top level limit, folowed by reference (MSL)
                    // -- AL, unit (non if FT), bottom level limit, folowed by reference (MSL)

                    //

                    if (l.startsWith("AC ")) {
                        if ((airspace != null) && (airspace.coordinates.size()>3) && !circle
                                && !airspace.coordinates.get(0).equals(airspace.coordinates.get(airspace.coordinates.size()-1)))
                            airspace.coordinates.add(airspace.coordinates.get(0));
                        if ((airspace != null) && (airspace.Name == null))
                            this.remove(airspace);
                        if ((airspace != null) && (airspace.coordinates.size()<4))
                            this.remove(airspace);
                        airspace = new Airspace();
                        airspace.Country = country;
                        cw = true;
                        //newAirspace = false;
                        this.add(airspace);
                        airspace.Version = "0";
                        airspace.ID = 0;
                        String c = l.replace("AC ", "").trim();
                        airspace.Category = AirspaceCategory.valueOf(Helpers.findRegex("[A-Za-z]+\\w|[A-Za-z]",c));
                    }
                    if (l.startsWith("AN ")) {
                        if (airspace != null) {
                            airspace.Name = l.replace("AN ", "");
                            //newAirspace = true;
                            System.out.println("Airspace: " + airspace.Name + " added Index: " + Integer.toString(this.size()-1) + " Country: " + airspace.Country );
                        }

                    }
                    if (l.startsWith("AH")) {
                        if (airspace != null) {
                            //String ss = Helpers.findRegex("\\d+", l);
                            //Integer sss = Integer.parseInt(ss);
                            airspace.AltLimit_Top = Integer.parseInt("0" + Helpers.findRegex("\\d+", l));
                            String m = Helpers.findRegex("(\\bMSL)|(\\bFL)|(\\bFT)|(\\bSFC)|(\\bUNLIM)|(\\bAGL)", l);
                            if (m.equals("UNLIM")) airspace.AltLimit_Top = 100000;
                            airspace.AltLimit_Top_Ref = Helpers.parseReference(m);
                            airspace.AltLimit_Top_Unit = Helpers.parseUnit(m);
                        }
                    }
                    if (l.startsWith("AL ")) {
                        if (airspace != null) {
                            airspace.AltLimit_Bottom = Integer.parseInt("0" + Helpers.findRegex("\\d+", l));
                            String m = Helpers.findRegex("(\\bMSL)|(\\bFL)|(\\bFT)|(\\bSFC)|(\\bUNLIM)|(\\bAGL)|(\\bGND)", l);
                            if (m.equals("UNLIM")) airspace.AltLimit_Top = 100000;
                            airspace.AltLimit_Bottom_Ref = Helpers.parseReference(m);
                            airspace.AltLimit_Bottom_Unit = Helpers.parseUnit(m);
                        }
                    }
                    if (l.startsWith("V D")) {
                        cw = (Helpers.findRegex("\\+", l).equals("+"));
                        int i = 0;
                    }
                    if (l.startsWith("V X")) {
                        center = Helpers.parseOpenAirLocation(l);
                    }
                    if (l.startsWith("DB ")) {
                        String[] be = l.split(",");
                        LatLng begin = Helpers.parseOpenAirLocation(be[0]);
                        LatLng end = Helpers.parseOpenAirLocation(be[1]);
                        airspace.coordinates.addAll(GeometricHelpers.drawArc(begin, end, center, cw));
                        circle = false;
                    }
                    if (l.startsWith("DP ")) {
                        location = Helpers.parseOpenAirLocation(l);
                        airspace.coordinates.add(new Coordinate(location.longitude, location.latitude));
                        circle = false;
                    }
                    if (l.startsWith("DC ")) {
                        if (airspace != null) {
                            String m = Helpers.findRegex("([0-9.]+\\w)|([0-9])", l);
                            airspace.coordinates.addAll(GeometricHelpers.drawCircle(center, Double.valueOf(m)));
                            circle = true;
                        }
                    }
    //                if (l.startsWith("SP"))
    //                {
    //                    // What if SP becomes before AN ??????????????
    //
    //                    // We need to check if the SP is just a pen setting which means this iy does not belong to a specific airspace
    //                    // If it does not belong to an airspace than delete this airspace
    //                    if (!newAirspace) {
    //                        if (airspace != null) {
    //                            this.remove(airspace);
    //                            airspace = null;
    //                        }
    //                    }
    //                }

                }
            }
            if ((airspace != null) && (airspace.coordinates.size()>0)) airspace.coordinates.add(airspace.coordinates.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void TestDraw(GoogleMap map)
//    {
//        Coordinate[] c1 = new Coordinate[14];
//        List<Coordinate> list = new ArrayList<Coordinate>();
//
//        LatLng l = Helpers.parseOpenAirLocation("DP 53:40:00 N 006:30:00 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 53:38:00 N 006:35:00 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 53:31:00 N 006:41:00 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 53:30:15 N 006:44:30 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 53:24:37 N 006:36:30 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 53:12:25 N 006:09:33 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 52:48:03 N 005:17:11 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 52:45:54 N 004:56:22 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 52:43:30 N 004:33:40 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 52:45:25 N 004:28:03 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 52:48:19 N 004:21:00 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 53:05:00 N 004:21:00 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 53:06:10 N 004:30:56 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 53:09:17 N 004:40:28 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//
//        LatLng center = Helpers.parseOpenAirLocation("V X=53:15:00 N 004:57:00 E");
//        LatLng begin = Helpers.parseOpenAirLocation("DB 53:11:06 N 004:38:03 E");
//        LatLng end = Helpers.parseOpenAirLocation("53:15:00 N 004:36:57 E");
//        list.addAll(GeometricHelpers.drawArc(begin, end, center));
//
//        center = Helpers.parseOpenAirLocation("V X=53:15:00 N 004:57:00 E");
//        begin = Helpers.parseOpenAirLocation("DB 53:15:00 N 004:43:38 E");
//        end = Helpers.parseOpenAirLocation("53:19:30 N 004:45:59 E");
//        list.addAll(GeometricHelpers.drawArc(begin, end, center));
//
//        center = Helpers.parseOpenAirLocation("V X=53:15:10 N 004:57:00 E");
//        begin = Helpers.parseOpenAirLocation("DB 53:19:30 N 004:45:59 E");
//        end = Helpers.parseOpenAirLocation("53:22:27 N 004:52:07 E");
//        list.addAll(GeometricHelpers.drawArc(begin, end, center));
//
//        l = Helpers.parseOpenAirLocation("DP 53:26:20 N 005:09:40 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 53:26:30 N 005:10:30 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//        l = Helpers.parseOpenAirLocation("DP 53:30:00 N 005:34:00 E");
//        list.add(new Coordinate(l.longitude, l.latitude));
//
//        list.add(list.get(0));
//
//        PolylineOptions o = new PolylineOptions();
//        o.color(Color.RED);
//        o.width(2);
//        o.zIndex(1000);
//        for (Coordinate coordinate : list)
//        {
//            LatLng p = new LatLng(coordinate.y, coordinate.x);
//            o.add(p);
//        }
//
//        Polyline p = map.addPolyline(o);
//        p.setVisible(true);
//        p.setZIndex(1000);
//
//    }
//
//    public void drawAirspace(Airspace airspace, GoogleMap map)
//    {
//        PolylineOptions o = new PolylineOptions();
//        o.color(Color.RED);
//        o.width(2);
//        o.zIndex(1000);
//        for (Coordinate coordinate : airspace.coordinates)
//        {
//            LatLng p = new LatLng(coordinate.y, coordinate.x);
//            o.add(p);
//        }
//
//        Polyline p = map.addPolyline(o);
//        p.setVisible(true);
//        p.setZIndex(1000);
//    }

    private void readOpenAipXML(String xml)
    {

        Airspace airspace = null;
        Boolean readAltTop = true;
        String name = "";

        try {
            Class.forName("org.xmlpull.v1.XmlPullParserFactory");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if(eventType == XmlPullParser.START_TAG) {
                    name = parser.getName();
                    if (name.equals("ASP")) {
                        airspace = new Airspace();
                        Add(airspace);
                        airspace.Category = AirspaceCategory.valueOf(parser.getAttributeValue(null, "CATEGORY"));
                    }

                    if (name.equals("ALTLIMIT_TOP")) {
                        readAltTop = true;
                        if (airspace != null) {
                            String a = parser.getAttributeValue(null, "REFERENCE");
                            if (a.equals("")) airspace.AltLimit_Top_Ref = AltitudeReference.MSL;
                            else
                            airspace.AltLimit_Top_Ref = AltitudeReference.valueOf(
                                    parser.getAttributeValue(null, "REFERENCE"));
                        }
                    }

                    if (name.equals("ALTLIMIT_BOTTOM")) {
                        readAltTop = false;
                        if (airspace != null) {
                            String a = parser.getAttributeValue(null, "REFERENCE");
                            if (a.equals("")) airspace.AltLimit_Bottom_Ref = AltitudeReference.MSL;
                            else
                            airspace.AltLimit_Bottom_Ref = AltitudeReference.valueOf(
                                    parser.getAttributeValue(null, "REFERENCE"));
                        }
                    }

                    if (name.equals("ALT")) {
                        if (readAltTop) {
                            if (airspace != null) {
                                airspace.AltLimit_Top_Unit = AltitudeUnit.valueOf(
                                        parser.getAttributeValue(null, "UNIT"));
                            }
                        } else {
                            if (airspace != null) {
                                airspace.AltLimit_Bottom_Unit = AltitudeUnit.valueOf(
                                        parser.getAttributeValue(null, "UNIT"));
                            }
                        }
                    }

                }
                if (eventType == XmlPullParser.TEXT) {
                    if (name.equals("VERSION")) {
                        if (airspace != null) airspace.Version = parser.getText();
                    }

                    if (name.equals("ID")) {
                        if (airspace != null) airspace.ID = Integer.parseInt(parser.getText());
                    }

                    if (name.equals("COUNTRY")) {
                        if (airspace != null) airspace.Country = parser.getText();
                    }

                    if (name.equals("NAME")) {
                        if (airspace != null) {
                            airspace.Name = parser.getText();
                            //if (progressDialog != null) progressDialog.setMessage("Loading Airspaces: " + airspace.Name);
                            System.out.println("Load Airspace: " + airspace.Name);
                        }
                    }



                    if (name.equals("ALT")) {
                        if (readAltTop) {
                            if (airspace != null) {
                                airspace.AltLimit_Top = Integer.parseInt(parser.getText());
                            }
                        } else {
                            if (airspace != null) {
                                airspace.AltLimit_Bottom = Integer.parseInt(parser.getText());
                            }
                        }
                    }
                    if (name.equals("POLYGON"))
                    {
                        if (airspace != null) {
                            String pol = parser.getText();
                            String pols[] = pol.split(",");
                            if (!pols[0].trim().equals(pols[pols.length-1])) pol = pol + ", " + pols[0];
                            String p = "POLYGON ((" + pol + "))";
                            WKTReader r = new WKTReader();
                            airspace.setGeometry(r.read(p));
                        }
                    }


                    name = "";
                }

                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String readFromFile(String fileName) {

        String ret = "";

        try {
            FileInputStream inputStream = new FileInputStream (new File(fileName));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int size = inputStream.available();
                char[] buffer = new char[size];

                inputStreamReader.read(buffer);

                inputStream.close();
                ret = new String(buffer);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
}
