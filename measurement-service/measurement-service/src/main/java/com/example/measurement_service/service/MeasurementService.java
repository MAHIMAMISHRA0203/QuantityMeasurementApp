package com.example.measurement_service.service;

import com.example.measurement_service.dto.QuantityDTO;
import com.example.measurement_service.entity.MeasurementOperationEntity;
import com.example.measurement_service.repository.MeasurementOperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MeasurementService {

    private final MeasurementOperationRepository measurementOperationRepository;

    // ✅ Conversion factors to base unit
    private static final Map<String, Double> LENGTH_TO_CM = new HashMap<>();
    private static final Map<String, Double> WEIGHT_TO_GRAM = new HashMap<>();
    private static final Map<String, Double> VOLUME_TO_ML = new HashMap<>();

    static {
        // Length to CM
        LENGTH_TO_CM.put("CENTIMETERS", 1.0);
        LENGTH_TO_CM.put("CENTIMETER", 1.0);
        LENGTH_TO_CM.put("METER", 100.0);
        LENGTH_TO_CM.put("METERS", 100.0);
        LENGTH_TO_CM.put("FEET", 30.48);
        LENGTH_TO_CM.put("FOOT", 30.48);
        LENGTH_TO_CM.put("INCHES", 2.54);
        LENGTH_TO_CM.put("INCH", 2.54);
        LENGTH_TO_CM.put("YARDS", 91.44);

        // Weight to GRAM
        WEIGHT_TO_GRAM.put("GRAM", 1.0);
        WEIGHT_TO_GRAM.put("KILOGRAM", 1000.0);
        WEIGHT_TO_GRAM.put("TONNE", 1000000.0);
        WEIGHT_TO_GRAM.put("POUND", 453.592);

        // Volume to ML
        VOLUME_TO_ML.put("MILLILITRE", 1.0);
        VOLUME_TO_ML.put("LITRE", 1000.0);
    }

    // ✅ Compare two quantities
    public boolean compare(QuantityDTO q1, QuantityDTO q2) {
        try {
            double base1 = toBaseUnit(q1);
            double base2 = toBaseUnit(q2);
            boolean result = Math.abs(base1 - base2) < 0.0001;
            saveSuccess("COMPARISON", q1, q2, String.valueOf(result));
            return result;
        } catch (RuntimeException ex) {
            saveError("COMPARISON", q1, q2, ex.getMessage());
            throw ex;
        }
    }

    // ✅ Convert quantity to target unit
    public QuantityDTO convert(QuantityDTO source, String targetUnit) {
        try {
            double baseValue = toBaseUnit(source);
            double targetFactor = getConversionFactor(
                    source.getMeasurementType(), targetUnit);
            double result = baseValue / targetFactor;
            QuantityDTO converted = new QuantityDTO(result, targetUnit, source.getMeasurementType());
            saveSuccess("CONVERSION", source, null, converted.getValue() + " " + converted.getUnit());
            return converted;
        } catch (RuntimeException ex) {
            saveError("CONVERSION", source, null, ex.getMessage());
            throw ex;
        }
    }

    // ✅ Add two quantities
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        try {
            double base1 = toBaseUnit(q1);
            double base2 = toBaseUnit(q2);
            double total = base1 + base2;
            QuantityDTO result = new QuantityDTO(total, getBaseUnit(q1.getMeasurementType()),
                    q1.getMeasurementType());
            saveSuccess("ADDITION", q1, q2, result.getValue() + " " + result.getUnit());
            return result;
        } catch (RuntimeException ex) {
            saveError("ADDITION", q1, q2, ex.getMessage());
            throw ex;
        }
    }

    // ✅ Subtract two quantities
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        try {
            double base1 = toBaseUnit(q1);
            double base2 = toBaseUnit(q2);
            double result = base1 - base2;
            QuantityDTO quantityResult = new QuantityDTO(result, getBaseUnit(q1.getMeasurementType()),
                    q1.getMeasurementType());
            saveSuccess("SUBTRACTION", q1, q2, quantityResult.getValue() + " " + quantityResult.getUnit());
            return quantityResult;
        } catch (RuntimeException ex) {
            saveError("SUBTRACTION", q1, q2, ex.getMessage());
            throw ex;
        }
    }

    // ✅ Divide two quantities
    public double divide(QuantityDTO q1, QuantityDTO q2) {
        try {
            double base1 = toBaseUnit(q1);
            double base2 = toBaseUnit(q2);
            if (base2 == 0) {
                throw new ArithmeticException("Cannot divide by zero");
            }
            double result = base1 / base2;
            saveSuccess("DIVISION", q1, q2, String.valueOf(result));
            return result;
        } catch (RuntimeException ex) {
            saveError("DIVISION", q1, q2, ex.getMessage());
            throw ex;
        }
    }

    private double toBaseUnit(QuantityDTO q) {
        double factor = getConversionFactor(q.getMeasurementType(), q.getUnit());
        return q.getValue() * factor;
    }

    private double getConversionFactor(String type, String unit) {
        return switch (type.toUpperCase()) {
            case "LENGTH"      -> LENGTH_TO_CM.getOrDefault(unit.toUpperCase(), 1.0);
            case "WEIGHT"      -> WEIGHT_TO_GRAM.getOrDefault(unit.toUpperCase(), 1.0);
            case "VOLUME"      -> VOLUME_TO_ML.getOrDefault(unit.toUpperCase(), 1.0);
            case "TEMPERATURE" -> 1.0;
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

    private String getBaseUnit(String type) {
        return switch (type.toUpperCase()) {
            case "LENGTH"      -> "CENTIMETERS";
            case "WEIGHT"      -> "GRAM";
            case "VOLUME"      -> "MILLILITRE";
            case "TEMPERATURE" -> "CELSIUS";
            default -> "UNKNOWN";
        };
    }

    private void saveSuccess(String operation, QuantityDTO q1, QuantityDTO q2, String result) {
        MeasurementOperationEntity entity = MeasurementOperationEntity.builder()
                .operation(operation)
                .operand1(formatQuantity(q1))
                .operand2(formatQuantity(q2))
                .result(result)
                .build();
        measurementOperationRepository.save(entity);
    }

    private void saveError(String operation, QuantityDTO q1, QuantityDTO q2, String error) {
        MeasurementOperationEntity entity = MeasurementOperationEntity.builder()
                .operation(operation)
                .operand1(formatQuantity(q1))
                .operand2(formatQuantity(q2))
                .error(error)
                .build();
        measurementOperationRepository.save(entity);
    }

    private String formatQuantity(QuantityDTO quantity) {
        if (quantity == null) {
            return null;
        }
        return quantity.getValue() + " " + quantity.getUnit();
    }
}
