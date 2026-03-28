package com.example.quantity_measurement_app.unit;

import com.example.quantity_measurement_app.model.IMeasurable;
import com.example.quantity_measurement_app.model.SupportsArithmetic;
import java.util.function.Function;

public enum TemperatureUnit implements IMeasurable{
    CELSIUS(
            c -> c,
            c -> c
    ),

    FAHRENHEIT(
            f -> (f - 32) * 5 / 9,
            c -> (c * 9 / 5) + 32
    );

    private final Function<Double, Double> toCelsius;
    private final Function<Double, Double> fromCelsius;

    // Temperature does not support arithmetic
    private SupportsArithmetic supportsArithmetic = () -> false;

    TemperatureUnit(Function<Double, Double> toCelsius,
                    Function<Double, Double> fromCelsius){
        this.toCelsius = toCelsius;
        this.fromCelsius = fromCelsius;
    }

    @Override
    public String getUnitName(){
        return name();
    }

    @Override
    public double getConversionFactor(){
        return 1.0;
    }

    @Override
    public double convertToBaseUnit(double value){
        return toCelsius.apply(value);
    }

    @Override
    public double convertFromBaseUnit(double baseValue){
        return fromCelsius.apply(baseValue);
    }

    public double convertTo(double value, TemperatureUnit targetUnit) {
        double base = convertToBaseUnit(value);
        return targetUnit.convertFromBaseUnit(base);
    }

    @Override
    public boolean supportsArithmetic(){
        return supportsArithmetic.isSupported();
    }

    @Override
    public void validateOperationSupport(String operation){
        throw new UnsupportedOperationException(
                "Temperature does not support " + operation + " operation."
        );
    }

    @Override
    public String getMeasurementType() {
        return "TEMPERATURE";
    }

    @Override
    public String toString() {
        return name();
    }
}
