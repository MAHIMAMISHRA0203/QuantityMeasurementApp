/*
 * Base interface for all measurable units.
 * Provides conversion methods and optional arithmetic support validation.
 */
package com.example.util;

public interface IMeasurable{

    // Default lambda -> all units support arithmetic by default
    SupportsArithmetic supportsArithmetic = () -> true;

    // ---------- Mandatory conversion methods ----------

    String getUnitName();
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    // ---------- Optional arithmetic capability ----------
    //Indicates whether arithmetic operations are supported.
     
    default boolean supportsArithmetic(){
        return supportsArithmetic.isSupported();
    }
    /**
     * Validates if an arithmetic operation is supported.
     * Default implementation allows all operations.
     * Units like TemperatureUnit can override this.
     */
    default void validateOperationSupport(String operation){
        // default: allow operations
    }

    /**
     * Gets the measurement type (e.g., LENGTH, WEIGHT, VOLUME, TEMPERATURE)
     * @return the measurement type category
     */
    String getMeasurementType();

    /**
     * Gets a unit instance based on the unit name
     * @param unitName the name of the unit (e.g., "FEET", "KILOGRAM")
     * @return the corresponding unit instance, or null if not found
     */
    static IMeasurable getUnitInstance(String unitName) {
        // Try to find unit in all unit types
        try {
            // Try LengthUnit
            return LengthUnit.valueOf(unitName);
        } catch (IllegalArgumentException e1) {
            try {
                // Try WeightUnit
                return WeightUnit.valueOf(unitName);
            } catch (IllegalArgumentException e2) {
                try {
                    // Try VolumeUnit
                    return VolumeUnit.valueOf(unitName);
                } catch (IllegalArgumentException e3) {
                    try {
                        // Try TemperatureUnit
                        return TemperatureUnit.valueOf(unitName);
                    } catch (IllegalArgumentException e4) {
                        return null;
                    }
                }
            }
        }
    }
}