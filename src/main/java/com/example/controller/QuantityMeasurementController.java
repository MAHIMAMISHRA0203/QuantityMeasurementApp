package com.example.controller;
import com.example.dto.QuantityDTO;
import com.example.exception.QuantityMeasurementException;
import com.example.service.QuantityMeasurementService;

/**
 * Controller layer for Quantity Measurement operations.
 * 
 * This controller handles requests related to quantity measurements, including
 * comparison, conversion, and arithmetic operations. It delegates business
 * logic
 * to the service layer and formats responses appropriately.
 * 
 * The controller is designed to be easily adaptable for REST API endpoints,
 * where the performXXX methods can be mapped to specific HTTP verbs and
 * endpoints.
 */
public class QuantityMeasurementController {

    private final QuantityMeasurementService service;

    /**
     * Constructor for dependency injection of the service layer.
     * 
     * @param service the IQuantityMeasurementService implementation
     */
    public QuantityMeasurementController(QuantityMeasurementService service) {
        this.service = service;
    }

    // ================= COMPARISON =================

    /**
     * Performs comparison of two quantities.
     * 
     * @param q1 the first quantity
     * @param q2 the second quantity
     * @return true if quantities are equal, false otherwise
     * @throws QuantityMeasurementException if quantities cannot be compared
     */
    public boolean performComparison(QuantityDTO q1, QuantityDTO q2)
            throws QuantityMeasurementException {
        return service.compare(q1, q2);
    }

    // ================= CONVERSION =================

    /**
     * Performs unit conversion of a quantity.
     * 
     * @param source     the source quantity
     * @param targetUnit the target unit to convert to
     * @return the converted quantity
     * @throws QuantityMeasurementException if conversion cannot be performed
     */
    public QuantityDTO performConversion(QuantityDTO source, QuantityDTO targetUnit)
            throws QuantityMeasurementException {
        return service.convert(source, targetUnit.getUnit());
    }

    // ================= ADDITION =================

    /**
     * Performs addition of two quantities.
     * 
     * @param q1 the first quantity
     * @param q2 the second quantity
     * @return the sum of the two quantities in the unit of q1
     * @throws QuantityMeasurementException if quantities cannot be added
     */
    public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2)
            throws QuantityMeasurementException {
        return service.add(q1, q2);
    }

    // ================= SUBTRACTION =================

    /**
     * Performs subtraction of two quantities.
     * 
     * @param q1 the first quantity
     * @param q2 the second quantity
     * @return the difference of the two quantities in the unit of q1
     * @throws QuantityMeasurementException if quantities cannot be subtracted
     */
    public QuantityDTO performSubtraction(QuantityDTO q1, QuantityDTO q2)
            throws QuantityMeasurementException {
        return service.subtract(q1, q2);
    }

    // ================= DIVISION =================

    /**
     * Performs division of two quantities.
     * 
     * @param q1 the dividend quantity
     * @param q2 the divisor quantity
     * @return the ratio of the two quantities
     * @throws QuantityMeasurementException if division cannot be performed
     */
    public double performDivision(QuantityDTO q1, QuantityDTO q2)
            throws QuantityMeasurementException {
        return service.divide(q1, q2);
    }
}