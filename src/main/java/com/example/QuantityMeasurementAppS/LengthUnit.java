package com.example.QuantityMeasurementAppS;

public enum LengthUnit {

    FEET(1.0),        // Base unit
    INCH(1.0 / 12.0); // 1 inch = 1/12 feet

    private final double conversionFactorToFeet;

    LengthUnit(double conversionFactorToFeet) {
        this.conversionFactorToFeet = conversionFactorToFeet;
    }

    public double toFeet(double value) {
        return value * conversionFactorToFeet;
    }
}
