package com.example.QuantityMeasurementAppS;

public class App {

    public static void main(String[] args) {

        boolean inchResult =
                FeetServices.areInchesEqual(1.0, 1.0);

        boolean feetResult =
                FeetServices.areFeetEqual(1.0, 1.0);

        System.out.println("1.0 inch == 1.0 inch ? " + inchResult);
        System.out.println("1.0 ft == 1.0 ft ? " + feetResult);
    }
}
