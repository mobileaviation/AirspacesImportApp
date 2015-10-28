package com.mobileaviationtools.AirspacesData;

/**
 * Created by Rob Verhoef on 20-10-2015.
 */
public enum AltitudeReference {
    GND,        // Ground
    MSL,        // Main sea level
    STD,        // Standard atmosphere
    AGL         // Above Ground level
    ;

    @Override
    public String toString() {
        return super.toString();
    }

//    @Override
//    public AltitudeReference valueOf(String value)
//    {
//        if (value.equals("")) return MSL;
//        else return super.valueOf(value);
//    }
}
