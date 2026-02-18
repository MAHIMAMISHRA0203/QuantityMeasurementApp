package com.example.QuantityMeasurementAppS;



public class FeetServices {

    public boolean areEqual(FeetPOJO f1, FeetPOJO f2) {

        if (f1 == null || f2 == null) {
            return false;
        }

        return f1.equals(f2);
    }
}
