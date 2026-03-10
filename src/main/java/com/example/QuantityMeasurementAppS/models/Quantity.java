package com.example.QuantityMeasurementAppS.models;

import com.example.QuantityMeasurementAppS.units.IMeasurable;
import com.example.QuantityMeasurementAppS.util.ArithmaticOperation;

public class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {

        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        this.value = value;
        this.unit = unit;
    }

    public Quantity<U> convertTo(U targetUnit) {

        double baseValue = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(converted, targetUnit);
    }

    private double performBaseArithmetic(Quantity<U> other,
                                         ArithmeticOperation operation) {

        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return operation.compute(base1, base2);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {

        double baseResult =
                performBaseArithmetic(other, ArithmeticOperation.ADD);

        double converted = targetUnit.convertFromBaseUnit(baseResult);

        return new Quantity<>(converted, targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

        double baseResult =
                performBaseArithmetic(other, ArithmaticOperation.SUBTRACT);

        double converted = targetUnit.convertFromBaseUnit(baseResult);

        return new Quantity<>(converted, targetUnit);
    }

    public double divide(Quantity<U> other) {

        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}