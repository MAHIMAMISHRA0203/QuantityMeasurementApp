package com.example.model;

import com.example.util.IMeasurable;

public class QuantityModel2<U extends IMeasurable> {

    private double value;
    private U unit;

    public QuantityModel2(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }
}