package com.example.quantity_measurement_app.model;

import com.example.quantity_measurement_app.unit.TemperatureUnit;
import java.util.Objects;

public final class Quantity<U extends IMeasurable>{
    private final double value;
    private final U unit;
    private static final double EPS = 0.0001;

    public Quantity(double value, U unit){

        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid numeric value");
        this.value = value;
        this.unit = unit;
    }

    public double getValue(){
        return value;
    }

    public U getUnit(){
        return unit;
    }

    // ================= VALIDATION =================

    private void validateOperand(Quantity<U> other){
        if (other == null)
            throw new IllegalArgumentException("Quantity cannot be null");
        if (!this.unit.getClass().equals(other.unit.getClass()))
            throw new IllegalArgumentException("Cross-category operation not allowed");
        if (!Double.isFinite(other.value))
            throw new IllegalArgumentException("Invalid operand value");
    }

    private double toBase(){
        return unit.convertToBaseUnit(value);
    }

    private static double round2(double v){
        return Math.round(v * 100.0) / 100.0;
    }

    // ================= CONVERSION =================
    public Quantity<U> convertTo(U targetUnit){
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");
        // Special handling for Temperature (non-linear conversion)
        if (unit instanceof TemperatureUnit && targetUnit instanceof TemperatureUnit) {

            TemperatureUnit source = (TemperatureUnit) unit;
            TemperatureUnit target = (TemperatureUnit) targetUnit;

            double converted = source.convertTo(value, target);

            return new Quantity<>(round2(converted), targetUnit);
        }
        // Default conversion for linear units
        double base = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);

        return new Quantity<>(round2(converted), targetUnit);
    }

    // ================= ADD =================
    public Quantity<U> add(Quantity<U> other){
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit){
        validateOperand(other);

        unit.validateOperationSupport("addition");

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseResult = this.toBase() + other.toBase();
        double converted = targetUnit.convertFromBaseUnit(baseResult);

        return new Quantity<>(round2(converted), targetUnit);
    }

    // ================= SUBTRACT =================

    public Quantity<U> subtract(Quantity<U> other){
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit){
        validateOperand(other);

        unit.validateOperationSupport("subtraction");

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseResult = this.toBase() - other.toBase();
        double converted = targetUnit.convertFromBaseUnit(baseResult);

        return new Quantity<>(round2(converted), targetUnit);
    }

    // ================= DIVIDE =================

    public double divide(Quantity<U> other) {
        validateOperand(other);

        unit.validateOperationSupport("division");

        double divisorBase = other.toBase();

        if (divisorBase == 0.0)
            throw new ArithmeticException("Division by zero");

        return this.toBase() / divisorBase;
    }

    // ================= EQUALITY =================
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Quantity<?> other))
            return false;
        if (!this.unit.getClass().equals(other.unit.getClass()))
            return false;
        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return Math.abs(base1 - base2) < EPS;
    }

    @Override
    public int hashCode(){
        return Objects.hash(round2(unit.convertToBaseUnit(value)), unit.getClass());
    }

    @Override
    public String toString(){
        return "Quantity(" + value + ", " + unit + ")";
    }
}
