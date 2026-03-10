package com.example.QuantityMeasurementAppS.App;

import com.example.QuantityMeasurementAppS.models.Quantity;
import com.example.QuantityMeasurementAppS.units.LengthUnit;
import com.example.QuantityMeasurementAppS.units.TemperatureUnit;
import com.example.QuantityMeasurementAppS.units.VolumeUnit;
import com.example.QuantityMeasurementAppS.units.WeightUnit;


public class App {

    public static void main(String[] args) {

        // LENGTH
        Quantity<LengthUnit> l1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12, LengthUnit.INCHES);

        System.out.println(l1.add(l2));

        // WEIGHT
        Quantity<WeightUnit> w1 = new Quantity<>(1, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000, WeightUnit.GRAM);

        System.out.println(w1.equals(w2));

        // VOLUME
        Quantity<VolumeUnit> v1 = new Quantity<>(1, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000, VolumeUnit.MILLILITRE);

        System.out.println(v1.add(v2));

        // TEMPERATURE
        Quantity<TemperatureUnit> t1 = new Quantity<>(0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> t2 = new Quantity<>(32, TemperatureUnit.FAHRENHEIT);

        System.out.println(t1.equals(t2));

        System.out.println(t1.convertTo(TemperatureUnit.FAHRENHEIT));
    }
}