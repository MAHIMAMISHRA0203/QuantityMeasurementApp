package com.example.quantity_measurement_app;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;

public class TestRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);
    
    public static void main(String[] args) {
        logger.info("Starting Quantity Measurement Application Tests");
        logger.info("UC1-UC15 tests: Quantity measurement operations (comparison, conversion, arithmetic)");
        logger.info("UC16 tests: Database persistence with JDBC and connection pooling");
        
        Class<?> testClass = QuantityMeasurementIntegrationTest.class;
        int testCount = 0;
        int passedCount = 0;
        int failedCount = 0;
        
        System.out.println("\n========== Running QuantityMeasurementIntegrationTest ==========\n");
        
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                testCount++;
                String testName = method.getName();
                DisplayName displayName = method.getAnnotation(DisplayName.class);
                if (displayName != null) {
                    testName = displayName.value();
                }
                
                try {
                    QuantityMeasurementIntegrationTest testInstance = new QuantityMeasurementIntegrationTest();
                    method.invoke(testInstance);
                    System.out.println("✓ PASSED: " + testName);
                    logger.debug("Test PASSED: {}", testName);
                    passedCount++;
                } catch (Exception e) {
                    System.out.println("✗ FAILED: " + testName);
                    System.out.println("  Error: " + e.getCause());
                    logger.error("Test FAILED: {} - {}", testName, e.getCause());
                    failedCount++;
                }
            }
        }
        
        System.out.println("\n========== Test Results ==========");
        System.out.println("Total Tests: " + testCount);
        System.out.println("Passed: " + passedCount);
        System.out.println("Failed: " + failedCount);
        System.out.println("Success Rate: " + (testCount > 0 ? (passedCount * 100 / testCount) : 0) + "%");
        
        logger.info("Test Execution Summary - Total: {}, Passed: {}, Failed: {}", testCount, passedCount, failedCount);
        
        if (failedCount == 0) {
            System.out.println("\n✓ All tests passed!");
            logger.info("All tests passed successfully! ✓");
        } else {
            System.out.println("\n✗ Some tests failed!");
            logger.warn("Some tests failed. Please review the errors above.");
        }
    }
}
