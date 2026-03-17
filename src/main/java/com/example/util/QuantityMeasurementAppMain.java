package com.example.util;

import com.example.controller.QuantityMeasurementController;
import com.example.dto.QuantityDTO;
import com.example.exception.QuantityMeasurementException;

/**
 * Main entry point for the Quantity Measurement Application.
 * 
 * This demonstration class shows how to use the N-Tier architecture
 * to perform various quantity measurement operations including comparison,
 * conversion, and arithmetic operations on different units of measurement.
 */
public class QuantityMeasurementAppMain {

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("Quantity Measurement Application");
        System.out.println("====================================\n");

        // Initialize the application and get the controller
        QuantityMeasurementController controller = 
            QuantityMeasurementApp.getInstance().controller;

        System.out.println("Application initialized successfully!\n");

        try {
            // ============= LENGTH OPERATIONS =============
            System.out.println("========= LENGTH COMPARISONS =========");
            demonstrateLengthComparison(controller);

            // ============= WEIGHT OPERATIONS =============
            System.out.println("\n========= WEIGHT OPERATIONS =========");
            demonstrateWeightOperations(controller);

            // ============= VOLUME OPERATIONS =============
            System.out.println("\n========= VOLUME OPERATIONS =========");
            demonstrateVolumeOperations(controller);

            // ============= CONVERSION OPERATIONS =============
            System.out.println("\n========= CONVERSION OPERATIONS =========");
            demonstrateConversions(controller);

            // ============= ERROR HANDLING =============
            System.out.println("\n========= ERROR HANDLING =========");
            demonstrateErrorHandling(controller);

            System.out.println("\n====================================");
            System.out.println("Application Test Completed Successfully!");
            System.out.println("====================================");

        } catch (Exception e) {
            System.err.println("Error during execution: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates length comparison operations.
     */
    private static void demonstrateLengthComparison(QuantityMeasurementController controller) 
            throws QuantityMeasurementException {
        System.out.println("\n1. Comparing 1 FEET with 12 INCH:");
        QuantityDTO feet = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO inches = new QuantityDTO(12.0, "INCH", "LENGTH");
        
        boolean result = controller.performComparison(feet, inches);
        System.out.println("   Result: " + result + " (Expected: true)");

        System.out.println("\n2. Comparing 3 YARDS with 9 FEET:");
        QuantityDTO yards = new QuantityDTO(3.0, "YARDS", "LENGTH");
        QuantityDTO feet2 = new QuantityDTO(9.0, "FEET", "LENGTH");
        
        result = controller.performComparison(yards, feet2);
        System.out.println("   Result: " + result + " (Expected: true)");
    }

    /**
     * Demonstrates weight operations.
     */
    private static void demonstrateWeightOperations(QuantityMeasurementController controller) 
            throws QuantityMeasurementException {
        System.out.println("\n1. Adding 1 KILOGRAM + 500 GRAM:");
        QuantityDTO kg = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        QuantityDTO gram = new QuantityDTO(500.0, "GRAM", "WEIGHT");
        
        QuantityDTO result = controller.performAddition(kg, gram);
        System.out.println("   Result: " + result.getValue() + " " + result.getUnit());

        System.out.println("\n2. Subtracting 2 KILOGRAM - 500 GRAM:");
        QuantityDTO kg2 = new QuantityDTO(2.0, "KILOGRAM", "WEIGHT");
        
        result = controller.performSubtraction(kg2, gram);
        System.out.println("   Result: " + result.getValue() + " " + result.getUnit());

        System.out.println("\n3. Dividing 1 KILOGRAM / 500 GRAM:");
        double division = controller.performDivision(kg, gram);
        System.out.println("   Result: " + division);
    }

    /**
     * Demonstrates volume operations.
     */
    private static void demonstrateVolumeOperations(QuantityMeasurementController controller) 
            throws QuantityMeasurementException {
        System.out.println("\n1. Comparing 1 LITRE with 1000 MILLILITRE:");
        QuantityDTO litre = new QuantityDTO(1.0, "LITRE", "VOLUME");
        QuantityDTO millilitre = new QuantityDTO(1000.0, "MILLILITRE", "VOLUME");
        
        boolean result = controller.performComparison(litre, millilitre);
        System.out.println("   Result: " + result + " (Expected: true)");

        System.out.println("\n2. Adding 2 LITRE + 500 MILLILITRE:");
        QuantityDTO litre2 = new QuantityDTO(2.0, "LITRE", "VOLUME");
        
        QuantityDTO addition = controller.performAddition(litre2, millilitre);
        System.out.println("   Result: " + addition.getValue() + " " + addition.getUnit());
    }

    /**
     * Demonstrates unit conversion operations.
     */
    private static void demonstrateConversions(QuantityMeasurementController controller) 
            throws QuantityMeasurementException {
        System.out.println("\n1. Converting 1 FEET to INCH:");
        QuantityDTO feet = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO targetInches = new QuantityDTO(0, "INCH", "LENGTH");
        
        QuantityDTO result = controller.performConversion(feet, targetInches);
        System.out.println("   Result: " + result.getValue() + " " + result.getUnit());

        System.out.println("\n2. Converting 1000 GRAM to KILOGRAM:");
        QuantityDTO grams = new QuantityDTO(1000.0, "GRAM", "WEIGHT");
        QuantityDTO targetKg = new QuantityDTO(0, "KILOGRAM", "WEIGHT");
        
        result = controller.performConversion(grams, targetKg);
        System.out.println("   Result: " + result.getValue() + " " + result.getUnit());

        System.out.println("\n3. Converting 1000 MILLILITRE to LITRE:");
        QuantityDTO ml = new QuantityDTO(1000.0, "MILLILITRE", "VOLUME");
        QuantityDTO targetL = new QuantityDTO(0, "LITRE", "VOLUME");
        
        result = controller.performConversion(ml, targetL);
        System.out.println("   Result: " + result.getValue() + " " + result.getUnit());
    }

    /**
     * Demonstrates error handling for invalid operations.
     */
    private static void demonstrateErrorHandling(QuantityMeasurementController controller) {
        System.out.println("\n1. Attempting to compare LENGTH with WEIGHT (should fail):");
        try {
            QuantityDTO feet = new QuantityDTO(1.0, "FEET", "LENGTH");
            QuantityDTO kg = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
            
            controller.performComparison(feet, kg);
            System.out.println("   ERROR: Should have thrown exception!");
        } catch (QuantityMeasurementException e) {
            System.out.println("   ✓ Correctly caught exception: " + e.getMessage());
        }

        System.out.println("\n2. Attempting to divide by zero:");
        try {
            QuantityDTO kg1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
            QuantityDTO kg0 = new QuantityDTO(0.0, "KILOGRAM", "WEIGHT");
            
            controller.performDivision(kg1, kg0);
            System.out.println("   ERROR: Should have thrown exception!");
        } catch (QuantityMeasurementException e) {
            System.out.println("   ✓ Correctly caught exception: " + e.getMessage());
        }

        System.out.println("\n3. Attempting to convert between different measurement types:");
        try {
            QuantityDTO feet = new QuantityDTO(1.0, "FEET", "LENGTH");
            QuantityDTO targetKg = new QuantityDTO(0, "KILOGRAM", "WEIGHT");
            
            controller.performConversion(feet, targetKg);
            System.out.println("   ERROR: Should have thrown exception!");
        } catch (QuantityMeasurementException e) {
            System.out.println("   ✓ Correctly caught exception: " + e.getMessage());
        }
    }
}