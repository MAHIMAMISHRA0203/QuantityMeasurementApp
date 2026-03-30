package com.example.quantity_measurement_app.unit;

import com.example.quantity_measurement_app.model.IMeasurable;

public enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),     // 1 mL = 0.001 L
    GALLON(3.78541);       // 1 gallon ≈ 3.78541 L

    private final double factor;

    VolumeUnit(double factor){
        this.factor = factor;
    }

    // conversion factor relative to base unit (LITRE)
    @Override
    public double getConversionFactor() {
        return factor;
    }

    // convert value in THIS unit ->base unit (LITRE)
    @Override
    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    // convert value in base unit (LITRE) → THIS unit
    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factor;
    }

    // readable name for printing
    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public String getMeasurementType() {
        return "VOLUME";
    }
}
