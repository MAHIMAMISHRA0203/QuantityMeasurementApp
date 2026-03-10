package com.example.QuantityMeasurementAppS.App;

import com.example.QuantityMeasurementAppS.models.Quantity;
import com.example.QuantityMeasurementAppS.units.LengthUnit;
import com.example.QuantityMeasurementAppS.units.VolumeUnit;
import com.example.QuantityMeasurementAppS.units.WeightUnit;

public class App {

    public static void main(String[] args) {

        Quantity<LengthUnit> length1 =
                new Quantity<>(1, LengthUnit.FEET);

        Quantity<LengthUnit> length2 =
                new Quantity<>(12, LengthUnit.INCHES);

        System.out.println(length1.add(length2, LengthUnit.FEET));

        Quantity<WeightUnit> weight1 =
                new Quantity<>(1, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> weight2 =
                new Quantity<>(1000, WeightUnit.GRAM);

        System.out.println(weight1.add(weight2, WeightUnit.KILOGRAM));

        Quantity<VolumeUnit> volume1 =
                new Quantity<>(1, VolumeUnit.LITRE);

        Quantity<VolumeUnit> volume2 =
                new Quantity<>(1000, VolumeUnit.MILLILITRE);

        System.out.println(volume1.add(volume2, VolumeUnit.LITRE));
    }
}