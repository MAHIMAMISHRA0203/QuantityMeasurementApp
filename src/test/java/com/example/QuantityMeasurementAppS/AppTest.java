package com.example.QuantityMeasurementAppS;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementServiceTest {

    // -------- FEET TESTS --------

    @Test
    void testFeetEquality_SameValue() {
        assertTrue(
                FeetServices.areFeetEqual(1.0, 1.0));
    }

    @Test
    void testFeetEquality_DifferentValue() {
        assertFalse(
                QuantityMeasurementService.areFeetEqual(1.0, 2.0));
    }

    @Test
    void testFeetEquality_SameReference() {
        FeetPOJO f = new FeetPOJO(5.0);
        assertTrue(f.equals(f));
    }

    @Test
    void testFeetEquality_NullComparison() {
        FeetPOJO f = new FeetPOJO(1.0);
        assertFalse(f.equals(null));
    }

    @Test
    void testFeetEquality_NonNumericInput() {
        FeetPOJO f = new FeetPOJO(1.0);
        assertFalse(f.equals("invalid"));
    }

    // -------- INCH TESTS --------

    @Test
    void testInchEquality_SameValue() {
        assertTrue(
                FeetServices.areInchesEqual(2.0, 2.0));
    }

    @Test
    void testInchEquality_DifferentValue() {
        assertFalse(
                FeetServices.areInchesEqual(2.0, 3.0));
    }

    @Test
    void testInchEquality_NullComparison() {
        InchesPOJO i = new InchesPOJO(2.0);
        assertFalse(i.equals(null));
    }
}
