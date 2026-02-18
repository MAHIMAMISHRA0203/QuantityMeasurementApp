package com.example.QuantityMeasurementAppS;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class FeetServicesTest {

    FeetServices service = new FeetServices();

    @Test
    void testEquality_SameValue() {
        FeetPOJO f1 = new FeetPOJO(1.0);
        FeetPOJO f2 = new FeetPOJO(1.0);

        assertTrue(service.areEqual(f1, f2),
                "1.0 ft should be equal to 1.0 ft");
    }

    @Test
    void testEquality_DifferentValue() {
        FeetPOJO f1 = new FeetPOJO(1.0);
        FeetPOJO f2 = new FeetPOJO(2.0);

        assertFalse(service.areEqual(f1, f2),
                "1.0 ft should not equal 2.0 ft");
    }

    @Test
    void testEquality_NullFirstValue() {
        FeetPOJO f2 = new FeetPOJO(1.0);

        assertFalse(service.areEqual(null, f2),
                "Null should not be equal to any value");
    }

    @Test
    void testEquality_NullSecondValue() {
        FeetPOJO f1 = new FeetPOJO(1.0);

        assertFalse(service.areEqual(f1, null),
                "Any value should not be equal to null");
    }

    @Test
    void testEquality_BothNull() {
        assertFalse(service.areEqual(null, null),
                "Two null values should return false in service logic");
    }

    @Test
    void testEquality_SameReference() {
        FeetPOJO f1 = new FeetPOJO(5.0);

        assertTrue(service.areEqual(f1, f1),
                "Object must be equal to itself");
    }
}
