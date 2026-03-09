package com.example.QuantityMeasurementAppS;

public class App {

    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println(q1.convertTo(LengthUnit.INCHES));

        System.out.println(q1.add(q2, LengthUnit.FEET));

        QuantityLength q3 = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.YARDS);

        System.out.println(q3.equals(q4));

        QuantityLength q5 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q6 = new QuantityLength(3.0, LengthUnit.FEET);

        System.out.println(q5.add(q6, LengthUnit.YARDS));

        QuantityLength q7 = new QuantityLength(2.54, LengthUnit.CENTIMETERS);

        System.out.println(q7.convertTo(LengthUnit.INCHES));

        QuantityLength q8 = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength q9 = new QuantityLength(0.0, LengthUnit.INCHES);

        System.out.println(q8.add(q9, LengthUnit.FEET));

        // Direct enum conversion
        System.out.println(LengthUnit.FEET.convertToBaseUnit(12.0));
        System.out.println(LengthUnit.INCHES.convertToBaseUnit(12.0));
    }
}