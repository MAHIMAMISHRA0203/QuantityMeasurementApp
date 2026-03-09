package com.example.QuantityMeasurementAppS;
import java.util.Objects;

/**
 * Immutable value object representing a length measurement.
 * Supports equality and explicit unit-to-unit conversion.
 */
public final class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    private static final double EPSILON = 1e-9;

    public QuantityLength(double value, LengthUnit unit) {

        validate(value, unit);

        this.value = value;
        this.unit = unit;
    }

    /**
     * Static conversion API
     */
    public static double convert(double value,
                                 LengthUnit source,
                                 LengthUnit target) {

        validate(value, source);
        if (target == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        if (source == target)
            return value;

        // Normalize to base unit (feet)
        double baseValue = source.toFeet(value);

        // Convert to target unit
        return target.fromFeet(baseValue);
    }

    /**
     * Instance conversion method
     * Returns new immutable QuantityLength
     */
    public QuantityLength convertTo(LengthUnit target) {

        double convertedValue = convert(this.value, this.unit, target);

        return new QuantityLength(convertedValue, target);
    }

    private static void validate(double value, LengthUnit unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be finite");
    }

    private double toBaseUnit() {
        return unit.toFeet(value);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (!(obj instanceof QuantityLength)) return false;

        QuantityLength other = (QuantityLength) obj;

        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(toBaseUnit() / EPSILON));
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}