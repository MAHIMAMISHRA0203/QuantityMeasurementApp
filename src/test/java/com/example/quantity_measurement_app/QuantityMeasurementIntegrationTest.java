package com.example.quantity_measurement_app;

import com.example.quantity_measurement_app.controller.QuantityMeasurementController;
import com.example.quantity_measurement_app.dto.QuantityDTO;
import com.example.quantity_measurement_app.entity.QuantityMeasurementEntity;
import com.example.quantity_measurement_app.exception.QuantityMeasurementException;
import com.example.quantity_measurement_app.repository.QuantityMeasurementOperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring Boot Integration Tests for Quantity Measurement Application.
 * Tests the complete flow from Controller → Service → Repository → Database.
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Quantity Measurement Integration Tests")
public class QuantityMeasurementIntegrationTest {

    @Autowired
    private QuantityMeasurementController controller;

    @Autowired
    private QuantityMeasurementOperationRepository repository;

    @BeforeEach
    public void setUp() {
        // Clear database before each test
        repository.deleteAll();
    }

    // ================== LENGTH MEASUREMENTS ==================

    @Test
    @DisplayName("Test: 1 FEET equals 12 INCH")
    public void testFeetEqualsInches() throws QuantityMeasurementException {
        QuantityDTO feet = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO inches = new QuantityDTO(12.0, "INCH", "LENGTH");
        assertTrue(controller.performComparison(feet, inches));

        // Verify operation was saved to database
        List<QuantityMeasurementEntity> operations = repository.findByOperation("COMPARISON");
        assertFalse(operations.isEmpty());
    }

    @Test
    @DisplayName("Test: 1 YARDS equals 3 FEET")
    public void testYardsEqualsFeet() throws QuantityMeasurementException {
        QuantityDTO yards = new QuantityDTO(1.0, "YARDS", "LENGTH");
        QuantityDTO feet = new QuantityDTO(3.0, "FEET", "LENGTH");
        assertTrue(controller.performComparison(yards, feet));
    }

    // ================== WEIGHT MEASUREMENTS ==================

    @Test
    @DisplayName("Test: 1 KILOGRAM equals 1000 GRAM")
    public void testKilogramEqualsGrams() throws QuantityMeasurementException {
        QuantityDTO kg = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO gram = new QuantityDTO(1000.0, "GRAM", "WEIGHT");
        assertTrue(controller.performComparison(kg, gram));
    }

    @Test
    @DisplayName("Test: Add 1 KILOGRAM + 500 GRAM")
    public void testAddWeights() throws QuantityMeasurementException {
        QuantityDTO kg = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO gram = new QuantityDTO(500.0, "GRAM", "WEIGHT");
        QuantityDTO result = controller.performAddition(kg, gram);
        assertEquals(1.5, result.getValue());

        // Verify operation was persisted to database
        List<QuantityMeasurementEntity> operations = repository.findByOperation("ADDITION");
        assertEquals(1, operations.size());
        assertFalse(operations.get(0).hasError());
    }

    @Test
    @DisplayName("Test: Subtract 2 KILOGRAM - 500 GRAM")
    public void testSubtractWeights() throws QuantityMeasurementException {
        QuantityDTO kg = new QuantityDTO(2.0, "KILOGRAM", "WEIGHT");
        QuantityDTO gram = new QuantityDTO(500.0, "GRAM", "WEIGHT");
        QuantityDTO result = controller.performSubtraction(kg, gram);
        assertEquals(1.5, result.getValue());
    }

    @Test
    @DisplayName("Test: Divide 1 KILOGRAM / 500 GRAM")
    public void testDivideWeights() throws QuantityMeasurementException {
        QuantityDTO kg = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO gram = new QuantityDTO(500.0, "GRAM", "WEIGHT");
        double result = controller.performDivision(kg, gram);
        assertEquals(2.0, result);
    }

    // ================== VOLUME MEASUREMENTS ==================

    @Test
    @DisplayName("Test: 1 LITRE equals 1000 MILLILITRE")
    public void testLitreEqualsMillilitres() throws QuantityMeasurementException {
        QuantityDTO litre = new QuantityDTO(1.0, "LITRE", "VOLUME");
        QuantityDTO ml = new QuantityDTO(1000.0, "MILLILITRE", "VOLUME");
        assertTrue(controller.performComparison(litre, ml));
    }

    @Test
    @DisplayName("Test: Convert 1 FEET to INCH")
    public void testConvertFeetToInches() throws QuantityMeasurementException {
        QuantityDTO feet = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO inches = new QuantityDTO(0, "INCH", "LENGTH");
        QuantityDTO result = controller.performConversion(feet, inches);
        assertEquals(12.0, result.getValue());

        // Verify operation was saved
        List<QuantityMeasurementEntity> conversions = repository.findByOperation("CONVERSION");
        assertEquals(1, conversions.size());
    }

    // ================== ERROR HANDLING ==================

    @Test
    @DisplayName("Test: Cross-category comparison should fail")
    public void testCrossCategoryComparison() {
        QuantityDTO feet = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO kg = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");

        // Verify that cross-category comparison throws an exception
        assertThrows(QuantityMeasurementException.class,
            () -> controller.performComparison(feet, kg));
    }

    @Test
    @DisplayName("Test: Division by zero should fail")
    public void testDivisionByZero() {
        QuantityDTO kg1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO kg0 = new QuantityDTO(0.0, "KILOGRAM", "WEIGHT");

        // Verify that dividing by zero throws an exception
        assertThrows(QuantityMeasurementException.class,
            () -> controller.performDivision(kg1, kg0));
    }

    // ================== DATABASE PERSISTENCE ==================

    @Test
    @DisplayName("Test: All operations are persisted to database")
    public void testOperationsPersistence() throws QuantityMeasurementException {
        // Perform multiple operations
        QuantityDTO feet = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO inches = new QuantityDTO(12.0, "INCH", "LENGTH");
        controller.performComparison(feet, inches);

        QuantityDTO kg = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO gram = new QuantityDTO(500.0, "GRAM", "WEIGHT");
        controller.performAddition(kg, gram);

        // Verify all were saved
        long totalCount = repository.count();
        assertEquals(2, totalCount);

        // Verify specific operations
        long comparisonCount = repository.findByOperation("COMPARISON").size();
        long additionCount = repository.findByOperation("ADDITION").size();
        assertEquals(1, comparisonCount);
        assertEquals(1, additionCount);
    }

    @Test
    @DisplayName("Test: Count successful operations")
    public void testCountSuccessfulOperations() throws QuantityMeasurementException {
        QuantityDTO feet = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO inches = new QuantityDTO(12.0, "INCH", "LENGTH");
        assertTrue(controller.performComparison(feet, inches)); // Success

        // Verify operation returned correct result
        QuantityDTO kg = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        assertThrows(QuantityMeasurementException.class,
            () -> controller.performComparison(feet, kg)); // Error
    }
}
