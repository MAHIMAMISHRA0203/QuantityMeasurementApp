//import com.example.QuantityMeasurementApp.exception.*;
//import com.example.QuantityMeasurementApp.repository.*;
//import com.example.QuantityMeasurementApp.service.*;
package com.example.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.service.QuantityMeasurementServiceImpl;
import com.example.controller.QuantityMeasurementController;
import com.example.repository.QuantityMeasurementDatabaseRepository;
import com.example.repository.QuantityMeasurementRepository;
import com.example.repository.QuantityMeasurementCacheRepository;
//import com.example.service.QuantityMeasurementServiceImpl;
import com.example.dto.QuantityDTO;
import com.example.exception.QuantityMeasurementException;


public class QuantityMeasurementApp {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementApp.class);
    private static QuantityMeasurementApp instance;
    public final QuantityMeasurementController controller;
    private QuantityMeasurementRepository repository;

    private QuantityMeasurementApp() {
        try {
            // Get repository type from configuration (cache or database)
            String repositoryType = ApplicationConfig.getInstance().getRepositoryType();
            
            if ("database".equalsIgnoreCase(repositoryType)) {
                logger.info("Using MySQL Database Repository");
                QuantityMeasurementDatabaseRepository dbRepository = 
                        new QuantityMeasurementDatabaseRepository();
                dbRepository.initialize();  // Initialize connection pool and create schema
                this.repository = dbRepository;
            } else {
                logger.info("Using In-Memory Cache Repository");
                this.repository = QuantityMeasurementCacheRepository.getInstance();
            }
            
            QuantityMeasurementServiceImpl service =
                    new QuantityMeasurementServiceImpl(repository);

            controller = new QuantityMeasurementController(service);
            logger.info("Application initialized successfully");
            
        } catch (Exception e) {
            logger.error("Failed to initialize application", e);
            throw new RuntimeException("Application initialization failed", e);
        }
    }

    public static QuantityMeasurementApp getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementApp();
        }
        return instance;
    }

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("Quantity Measurement Application");
        System.out.println("====================================\n");

        try {
            // Initialize the application and get the controller
            QuantityMeasurementController controller =
                    QuantityMeasurementApp.getInstance().controller;

            System.out.println("✓ Application initialized successfully!\n");

            // ============= LENGTH COMPARISONS =============
            System.out.println("========= LENGTH COMPARISONS =========");
            
            QuantityDTO feet = new QuantityDTO(1.0, "FEET", "LENGTH");
            QuantityDTO inches = new QuantityDTO(12.0, "INCH", "LENGTH");
            
            System.out.println("1. Comparing 1 FEET with 12 INCH:");
            boolean result = controller.performComparison(feet, inches);
            System.out.println("   ✓ Result: " + result + " (Expected: true)\n");

            // ============= WEIGHT OPERATIONS =============
            System.out.println("========= WEIGHT OPERATIONS =========");
            
            QuantityDTO kg = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
            QuantityDTO gram = new QuantityDTO(500.0, "GRAM", "WEIGHT");
            
            System.out.println("2. Adding 1 KILOGRAM + 500 GRAM:");
            QuantityDTO addition = controller.performAddition(kg, gram);
            System.out.println("   ✓ Result: " + addition.getValue() + " " + addition.getUnit() + "\n");

            System.out.println("3. Subtracting 2 KILOGRAM - 500 GRAM:");
            QuantityDTO kg2 = new QuantityDTO(2.0, "KILOGRAM", "WEIGHT");
            QuantityDTO subtraction = controller.performSubtraction(kg2, gram);
            System.out.println("   ✓ Result: " + subtraction.getValue() + " " + subtraction.getUnit() + "\n");

            System.out.println("4. Dividing 1 KILOGRAM / 500 GRAM:");
            double division = controller.performDivision(kg, gram);
            System.out.println("   ✓ Result: " + division + "\n");

            // ============= VOLUME OPERATIONS =============
            System.out.println("========= VOLUME OPERATIONS =========");
            
            QuantityDTO litre = new QuantityDTO(1.0, "LITRE", "VOLUME");
            QuantityDTO millilitre = new QuantityDTO(1000.0, "MILLILITRE", "VOLUME");
            
            System.out.println("5. Comparing 1 LITRE with 1000 MILLILITRE:");
            result = controller.performComparison(litre, millilitre);
            System.out.println("   ✓ Result: " + result + " (Expected: true)\n");

            // ============= CONVERSIONS =============
            System.out.println("========= UNIT CONVERSIONS =========");
            
            System.out.println("6. Converting 1 FEET to INCH:");
            QuantityDTO targetInches = new QuantityDTO(0, "INCH", "LENGTH");
            QuantityDTO conversion = controller.performConversion(feet, targetInches);
            System.out.println("   ✓ Result: " + conversion.getValue() + " " + conversion.getUnit() + "\n");

            System.out.println("7. Converting 1000 GRAM to KILOGRAM:");
            QuantityDTO targetKg = new QuantityDTO(0, "KILOGRAM", "WEIGHT");
            conversion = controller.performConversion(gram, targetKg);
            System.out.println("   ✓ Result: " + conversion.getValue() + " " + conversion.getUnit() + "\n");

            // ============= ERROR HANDLING =============
            System.out.println("========= ERROR HANDLING DEMO =========");
            
            System.out.println("8. Attempting to compare LENGTH with WEIGHT (should fail):");
            try {
                QuantityDTO feet1 = new QuantityDTO(1.0, "FEET", "LENGTH");
                QuantityDTO kg1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
                controller.performComparison(feet1, kg1);
                System.out.println("   ✗ ERROR: Should have thrown exception!");
            } catch (QuantityMeasurementException e) {
                System.out.println("   ✓ Correctly caught: " + e.getMessage() + "\n");
            }

            System.out.println("9. Attempting to divide by zero:");
            try {
                QuantityDTO kg1 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
                QuantityDTO kg0 = new QuantityDTO(0.0, "KILOGRAM", "WEIGHT");
                controller.performDivision(kg1, kg0);
                System.out.println("   ✗ ERROR: Should have thrown exception!");
            } catch (QuantityMeasurementException e) {
                System.out.println("   ✓ Correctly caught: " + e.getMessage() + "\n");
            }

            System.out.println("====================================");
            System.out.println("✓ All tests completed successfully!");
            System.out.println("====================================");

        } catch (Exception e) {
            System.err.println("✗ Error during execution: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close database resources if using database repository
            try {
                String repositoryType = ApplicationConfig.getInstance().getRepositoryType();
                if ("database".equalsIgnoreCase(repositoryType)) {
                    logger.info("Closing database connections...");
                    System.out.println("\nClosing database connections...");
                }
            } catch (Exception e) {
                logger.error("Error during cleanup", e);
            }
        }
    }

    /**
     * Release all database resources
     */
    public void closeResources() {
        if (repository instanceof QuantityMeasurementDatabaseRepository) {
            ((QuantityMeasurementDatabaseRepository) repository).releaseResources();
            logger.info("Database resources released");
        }
    }
}