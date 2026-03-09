package com.example.QuantityMeasurementAppS;

public class App {

    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = q1.add(q2);

        System.out.println("Result: " + result);


        QuantityLength a = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);

        System.out.println("Result: " + a.add(b));


        QuantityLength c = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength d = new QuantityLength(3.0, LengthUnit.FEET);

        System.out.println("Result: " + c.add(d));


        QuantityLength e = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength f = new QuantityLength(1.0, LengthUnit.INCHES);

        System.out.println("Result: " + e.add(f));
    }
}