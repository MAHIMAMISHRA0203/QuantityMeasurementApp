package com.example.QuantityMeasurementAppS;

import java.util.Objects;

public class Quantity {

    private final double value;
    private final IMeasurable unit;

    public Quantity(double value, IMeasurable unit) {

        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        this.value = value;
        this.unit = unit;
    }

    public Quantity convertTo(IMeasurable targetUnit) {

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseValue = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity(converted, targetUnit);
    }

    public Quantity add(Quantity other) {

        if (other == null)
            throw new IllegalArgumentException("Second operand cannot be null");

        double baseSum =
                unit.convertToBaseUnit(value) +
                other.unit.convertToBaseUnit(other.value);

        double result = unit.convertFromBaseUnit(baseSum);

        return new Quantity(result, unit);
    }

    public Quantity add(Quantity other, IMeasurable targetUnit) {

        if (other == null)
            throw new IllegalArgumentException("Second operand cannot be null");

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseSum =
                unit.convertToBaseUnit(value) +
                other.unit.convertToBaseUnit(other.value);

        double result = targetUnit.convertFromBaseUnit(baseSum);

        return new Quantity(result, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Quantity))
            return false;

        Quantity other = (Quantity) obj;

        double epsilon = 0.0001;

        double thisBase = unit.convertToBaseUnit(value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        return Math.abs(thisBase - otherBase) < epsilon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.convertToBaseUnit(value));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}