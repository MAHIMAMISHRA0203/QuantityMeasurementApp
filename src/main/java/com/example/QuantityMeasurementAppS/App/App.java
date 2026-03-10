package com.example.QuantityMeasurementAppS.App;

import com.example.QuantityMeasurementAppS.Quantity;
import com.example.QuantityMeasurementAppS.units.IMeasurable;

public class App {

    public static void main(String[] args) {

        System.out.println("----- SUBTRACTION -----");

        Quantity<LengthUnit> length1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> length2 = new Quantity<>(6.0, LengthUnit.INCHES);

        System.out.println(length1.subtract(length2));
        System.out.println(length1.subtract(length2, LengthUnit.INCHES));

        Quantity<WeightUnit> weight1 = new Quantity<>(10.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> weight2 = new Quantity<>(5000.0, WeightUnit.GRAM);

        System.out.println(weight1.subtract(weight2));

        Quantity<VolumeUnit> volume1 = new Quantity<>(5.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> volume2 = new Quantity<>(500.0, VolumeUnit.MILLILITRE);

        System.out.println(volume1.subtract(volume2));


        System.out.println("\n----- DIVISION -----");

        System.out.println(new Quantity<>(10.0, LengthUnit.FEET)
                .divide(new Quantity<>(2.0, LengthUnit.FEET)));

        System.out.println(new Quantity<>(24.0, LengthUnit.INCHES)
                .divide(new Quantity<>(2.0, LengthUnit.FEET)));

        System.out.println(new Quantity<>(10.0, WeightUnit.KILOGRAM)
                .divide(new Quantity<>(5.0, WeightUnit.KILOGRAM)));

        System.out.println(new Quantity<>(5.0, VolumeUnit.LITRE)
                .divide(new Quantity<>(10.0, VolumeUnit.LITRE)));
    }
}


/* -------- LENGTH -------- */

enum LengthUnit implements IMeasurable {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public String getUnitName() {
        return name();
    }
}


/* -------- WEIGHT -------- */

enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public String getUnitName() {
        return name();
    }
}


/* -------- VOLUME -------- */

enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public String getUnitName() {
        return name();
    }
}