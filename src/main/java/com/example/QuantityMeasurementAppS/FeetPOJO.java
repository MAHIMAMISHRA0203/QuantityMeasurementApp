package com.example.QuantityMeasurementAppS;


public final class FeetPOJO {

    private final double value;

    public FeetPOJO(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        FeetPOJO other = (FeetPOJO) obj;

        return Double.compare(this.value, other.value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
}

