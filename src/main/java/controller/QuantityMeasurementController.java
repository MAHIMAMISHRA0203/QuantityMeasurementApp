package controller;

public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    // ================= COMPARISON =================

    public boolean performComparison(QuantityDTO q1, QuantityDTO q2) {
        try {
            return service.compare(q1, q2);
        } catch (Exception e) {
            return false;
        }
    }

    // ================= CONVERSION =================

    public QuantityDTO performConversion(QuantityDTO source, QuantityDTO targetUnit) {
        try {
            return service.convert(source, targetUnit.getUnit());
        } catch (Exception e) {
            return null;
        }
    }

    // ================= ADDITION =================

    public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2) {
        try {
            return service.add(q1, q2);
        } catch (Exception e) {
            return null;
        }
    }

    // ================= SUBTRACTION =================

    public QuantityDTO performSubtraction(QuantityDTO q1, QuantityDTO q2) {
        try {
            return service.subtract(q1, q2);
        } catch (Exception e) {
            return null;
        }
    }

    // ================= DIVISION =================

    public double performDivision(QuantityDTO q1, QuantityDTO q2) {
        try {
            return service.divide(q1, q2);
        } catch (Exception e) {
            return 0;
        }
    }
}

