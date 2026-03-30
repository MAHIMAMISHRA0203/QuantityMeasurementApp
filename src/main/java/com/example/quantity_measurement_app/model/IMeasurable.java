package com.example.quantity_measurement_app.model;

public interface IMeasurable {

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

    String getMeasurementType();

    static IMeasurable getUnitInstance(String unitName) {
            try {
            // Try LengthUnit
            return com.example.quantity_measurement_app.unit.LengthUnit.valueOf(unitName);
        } catch (IllegalArgumentException e1) {
            try {
                // Try WeightUnit
                return com.example.quantity_measurement_app.unit.WeightUnit.valueOf(unitName);
            } catch (IllegalArgumentException e2) {
                try {
                    // Try VolumeUnit
                    return com.example.quantity_measurement_app.unit.VolumeUnit.valueOf(unitName);
                } catch (IllegalArgumentException e3) {
                    try {
                        // Try TemperatureUnit
                        return com.example.quantity_measurement_app.unit.TemperatureUnit.valueOf(unitName);
                    } catch (IllegalArgumentException e4) {
                        return null;
                    }
                }
            }
        }
    }
}
