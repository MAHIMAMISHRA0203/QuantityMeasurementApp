package com.example.QuantityMeasurementAppS;

import java.util.Objects;

public class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {

        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (value < 0)
            throw new IllegalArgumentException("Value cannot be negative");

        this.value = value;
        this.unit = unit;
    }

    private double toBaseUnit() {
        return unit.toFeet(value);
    }

    @Override
    public boolean equals(Object obj) {

        // Reflexive
        if (this == obj) return true;

        // Null check
        if (obj == null) return false;

        // Type safety
        if (!(obj instanceof QuantityLength)) return false;

        QuantityLength other = (QuantityLength) obj;

        // Convert both to base unit and compare
        return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toBaseUnit());
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}
