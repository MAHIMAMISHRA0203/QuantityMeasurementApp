package com.example.entity;
import java.io.Serializable;

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

    public String getOperation() {
        return operation;
    }

    public String getOperand1() {
        return operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public String getResult() {
        return result;
    }

    public String getError() {
        return error;
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