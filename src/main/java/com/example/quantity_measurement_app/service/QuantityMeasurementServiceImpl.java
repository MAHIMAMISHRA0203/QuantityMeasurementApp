package com.example.quantity_measurement_app.service;

import com.example.quantity_measurement_app.model.IMeasurable;
import com.example.quantity_measurement_app.dto.QuantityDTO;
import com.example.quantity_measurement_app.entity.QuantityMeasurementEntity;
import com.example.quantity_measurement_app.exception.QuantityMeasurementException;
import com.example.quantity_measurement_app.repository.QuantityMeasurementOperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
@Transactional
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final QuantityMeasurementOperationRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean compare(QuantityDTO q1, QuantityDTO q2) {
        try {
            // Validate measurement types match
            if (!q1.getMeasurementType().equals(q2.getMeasurementType())) {
                String errorMsg = "Cross category comparison not allowed";
                saveError("COMPARISON", errorMsg);
                throw new QuantityMeasurementException(errorMsg);
            }

            // Get unit instances to access conversion methods
            IMeasurable unit1 = IMeasurable.getUnitInstance(q1.getUnit());
            IMeasurable unit2 = IMeasurable.getUnitInstance(q2.getUnit());

            if (unit1 == null || unit2 == null) {
                String errorMsg = "Invalid unit: " + (unit1 == null ? q1.getUnit() : q2.getUnit());
                saveError("COMPARISON", errorMsg);
                throw new QuantityMeasurementException(errorMsg);
            }

            // Convert both values to base unit for fair comparison
            double baseValue1 = unit1.convertToBaseUnit(q1.getValue());
            double baseValue2 = unit2.convertToBaseUnit(q2.getValue());

            boolean result = baseValue1 == baseValue2;

            // Save successful operation
            saveOperation("COMPARISON", String.valueOf(q1.getValue()) + " " + q1.getUnit(),
                    String.valueOf(q2.getValue()) + " " + q2.getUnit(),
                    String.valueOf(result));

            return result;

        } catch (QuantityMeasurementException e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = "Comparison failed: " + e.getMessage();
            saveError("COMPARISON", errorMsg);
            throw new QuantityMeasurementException(errorMsg, e);
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO source, String targetUnit) {
        try {
            // Get source and target unit instances
            IMeasurable sourceUnit = IMeasurable.getUnitInstance(source.getUnit());
            IMeasurable targetUnitObj = IMeasurable.getUnitInstance(targetUnit);

            if (sourceUnit == null || targetUnitObj == null) {
                String errorMsg = "Invalid unit conversion: " + source.getUnit() + " to " + targetUnit;
                saveError("CONVERSION", errorMsg);
                throw new QuantityMeasurementException(errorMsg);
            }

            // Verify both units are of same measurement type
            if (!sourceUnit.getMeasurementType().equals(targetUnitObj.getMeasurementType())) {
                String errorMsg = "Cannot convert between different measurement types: "
                    + sourceUnit.getMeasurementType() + " and " + targetUnitObj.getMeasurementType();
                saveError("CONVERSION", errorMsg);
                throw new QuantityMeasurementException(errorMsg);
            }

            // Perform conversion: source value → base unit → target unit
            double baseValue = sourceUnit.convertToBaseUnit(source.getValue());
            double convertedValue = targetUnitObj.convertFromBaseUnit(baseValue);

            QuantityDTO result = new QuantityDTO(convertedValue, targetUnit, source.getMeasurementType());

            // Save successful operation
            saveOperation("CONVERSION", String.valueOf(source.getValue()) + " " + source.getUnit(),
                    targetUnit, String.valueOf(convertedValue) + " " + targetUnit);

            return result;

        } catch (QuantityMeasurementException e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = "Conversion failed: " + e.getMessage();
            saveError("CONVERSION", errorMsg);
            throw new QuantityMeasurementException(errorMsg, e);
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        try {
            // Validate measurement types match
            if (!q1.getMeasurementType().equals(q2.getMeasurementType())) {
                String errorMsg = "Cross category addition not allowed";
                saveError("ADDITION", errorMsg);
                throw new QuantityMeasurementException(errorMsg);
            }

            // Get unit instances
            IMeasurable unit1 = IMeasurable.getUnitInstance(q1.getUnit());
            IMeasurable unit2 = IMeasurable.getUnitInstance(q2.getUnit());

            if (unit1 == null || unit2 == null) {
                String errorMsg = "Invalid unit: " + (unit1 == null ? q1.getUnit() : q2.getUnit());
                saveError("ADDITION", errorMsg);
                throw new QuantityMeasurementException(errorMsg);
            }

            // Convert both values to base unit, add, then convert back to q1's unit
            double baseValue1 = unit1.convertToBaseUnit(q1.getValue());
            double baseValue2 = unit2.convertToBaseUnit(q2.getValue());
            double baseSum = baseValue1 + baseValue2;
            double resultValue = unit1.convertFromBaseUnit(baseSum);

            QuantityDTO result = new QuantityDTO(resultValue, q1.getUnit(), q1.getMeasurementType());

            // Save successful operation
            saveOperation("ADDITION", String.valueOf(q1.getValue()) + " " + q1.getUnit(),
                    String.valueOf(q2.getValue()) + " " + q2.getUnit(),
                    String.valueOf(resultValue) + " " + q1.getUnit());

            return result;

        } catch (QuantityMeasurementException e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = "Addition failed: " + e.getMessage();
            saveError("ADDITION", errorMsg);
            throw new QuantityMeasurementException(errorMsg, e);
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        try {
            // Validate measurement types match
            if (!q1.getMeasurementType().equals(q2.getMeasurementType())) {
                String errorMsg = "Cross category subtraction not allowed";
                saveError("SUBTRACTION", errorMsg);
                throw new QuantityMeasurementException(errorMsg);
            }

            // Get unit instances
            IMeasurable unit1 = IMeasurable.getUnitInstance(q1.getUnit());
            IMeasurable unit2 = IMeasurable.getUnitInstance(q2.getUnit());

            if (unit1 == null || unit2 == null) {
                String errorMsg = "Invalid unit: " + (unit1 == null ? q1.getUnit() : q2.getUnit());
                saveError("SUBTRACTION", errorMsg);
                throw new QuantityMeasurementException(errorMsg);
            }

            // Convert both values to base unit, subtract, then convert back to q1's unit
            double baseValue1 = unit1.convertToBaseUnit(q1.getValue());
            double baseValue2 = unit2.convertToBaseUnit(q2.getValue());
            double baseDifference = baseValue1 - baseValue2;
            double resultValue = unit1.convertFromBaseUnit(baseDifference);

            QuantityDTO result = new QuantityDTO(resultValue, q1.getUnit(), q1.getMeasurementType());

            // Save successful operation
            saveOperation("SUBTRACTION", String.valueOf(q1.getValue()) + " " + q1.getUnit(),
                    String.valueOf(q2.getValue()) + " " + q2.getUnit(),
                    String.valueOf(resultValue) + " " + q1.getUnit());

            return result;

        } catch (QuantityMeasurementException e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = "Subtraction failed: " + e.getMessage();
            saveError("SUBTRACTION", errorMsg);
            throw new QuantityMeasurementException(errorMsg, e);
        }
    }

    @Override
    public double divide(QuantityDTO q1, QuantityDTO q2) {
        try {
            // Validate measurement types match
            if (!q1.getMeasurementType().equals(q2.getMeasurementType())) {
                String errorMsg = "Cross category division not allowed";
                saveError("DIVISION", errorMsg);
                throw new QuantityMeasurementException(errorMsg);
            }

            // Check for division by zero
            if (q2.getValue() == 0) {
                String errorMsg = "Division by zero";
                saveError("DIVISION", errorMsg);
                throw new QuantityMeasurementException(errorMsg);
            }

            // Get unit instances
            IMeasurable unit1 = IMeasurable.getUnitInstance(q1.getUnit());
            IMeasurable unit2 = IMeasurable.getUnitInstance(q2.getUnit());

            if (unit1 == null || unit2 == null) {
                String errorMsg = "Invalid unit: " + (unit1 == null ? q1.getUnit() : q2.getUnit());
                saveError("DIVISION", errorMsg);
                throw new QuantityMeasurementException(errorMsg);
            }

            // Convert both values to base unit for fair division
            double baseValue1 = unit1.convertToBaseUnit(q1.getValue());
            double baseValue2 = unit2.convertToBaseUnit(q2.getValue());
            double result = baseValue1 / baseValue2;

            // Save successful operation
            saveOperation("DIVISION", String.valueOf(q1.getValue()) + " " + q1.getUnit(),
                    String.valueOf(q2.getValue()) + " " + q2.getUnit(),
                    String.valueOf(result));

            return result;

        } catch (QuantityMeasurementException e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = "Division failed: " + e.getMessage();
            saveError("DIVISION", errorMsg);
            throw new QuantityMeasurementException(errorMsg, e);
        }
    }

    public void saveOperation(String operation, String operand1, String operand2, String result) {
        try {
            QuantityMeasurementEntity entity = QuantityMeasurementEntity.builder()
                    .operation(operation)
                    .operand1(operand1)
                    .operand2(operand2)
                    .result(result)
                    .build();
            repository.save(entity);
        } catch (Exception e) {
            System.err.println("Failed to save operation: " + e.getMessage());
        }
    }

    public void saveError(String operation, String error) {
        try {
            QuantityMeasurementEntity entity = QuantityMeasurementEntity.builder()
                    .operation(operation)
                    .error(error)
                    .build();
            repository.save(entity);
            System.out.println("Error saved: " + operation + " - " + error);
        } catch (Exception e) {
            System.err.println("Failed to save error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
