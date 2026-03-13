package entity;

public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String operation;
    private String operand1;
    private String operand2;
    private String result;
    private String error;

    public QuantityMeasurementEntity(String operation, String operand1,
                                     String operand2, String result) {
        this.operation = operation;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }

    public QuantityMeasurementEntity(String operation, String error) {
        this.operation = operation;
        this.error = error;
    }

    public boolean hasError() {
        return error != null;
    }

    @Override
    public String toString() {
        if (hasError()) {
            return "Operation=" + operation + " ERROR=" + error;
        }
        return "Operation=" + operation +
                ", operand1=" + operand1 +
                ", operand2=" + operand2 +
                ", result=" + result;
    }
}