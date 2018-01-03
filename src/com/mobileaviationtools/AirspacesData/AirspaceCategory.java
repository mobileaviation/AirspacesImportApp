package com.mobileaviationtools.AirspacesData;

import java.util.EnumSet;

/**
 * Created by Rob Verhoef on 20-10-2015.
 */
public enum AirspaceCategory {
    A,
    AWY,
    B,
    C,
    CTR,
    CTA,
    D,
    DANGER,
    Q,
    E,
    F,
    G,
    GP,
    GLIDING,
    GSEC,
    OTH,
    RESTRICTED,
    R,
    TMA,
    TMZ,
    TSA,
    WAVE,
    W,
    PROHIBITED,
    P,
    FIR,
    UIR,
    RMZ,
    Z,
    ZP,
    UKN;

    @Override
    public String toString() {
        return super.toString();
    }

    public static EnumSet<AirspaceCategory> doNotInsertSet()
    {
        return EnumSet.of(AWY, UKN);
    }
}
