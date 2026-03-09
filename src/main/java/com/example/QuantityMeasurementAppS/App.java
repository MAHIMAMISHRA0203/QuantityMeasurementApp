package com.example.QuantityMeasurementAppS;

public class App {

    public static void main(String[] args) {

        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println(a.add(b, LengthUnit.FEET));
        System.out.println(a.add(b, LengthUnit.INCHES));
        System.out.println(a.add(b, LengthUnit.YARDS));

        QuantityLength c = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength d = new QuantityLength(3.0, LengthUnit.FEET);

        System.out.println(c.add(d, LengthUnit.YARDS));

        QuantityLength e = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength f = new QuantityLength(1.0, LengthUnit.YARDS);

        System.out.println(e.add(f, LengthUnit.FEET));

        QuantityLength g = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength h = new QuantityLength(1.0, LengthUnit.INCHES);

        System.out.println(g.add(h, LengthUnit.CENTIMETERS));

        QuantityLength i = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength j = new QuantityLength(0.0, LengthUnit.INCHES);

        System.out.println(i.add(j, LengthUnit.YARDS));

        QuantityLength k = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength l = new QuantityLength(-2.0, LengthUnit.FEET);

        System.out.println(k.add(l, LengthUnit.INCHES));
    }
}