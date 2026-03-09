package com.example.QuantityMeasurementAppS;
public enum LengthUnit {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(0.393701 / 12.0);

    private final double conversionFactorToFeet;

    LengthUnit(double factor) {
        this.conversionFactorToFeet = factor;
    }

    public double toFeet(double value) {
        return value * conversionFactorToFeet;
    }

    public double fromFeet(double feetValue) {
        return feetValue / conversionFactorToFeet;
    }

    public double getConversionFactor() {
        return conversionFactorToFeet;
    }
}