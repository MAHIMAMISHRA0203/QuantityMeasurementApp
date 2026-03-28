-- Quantity Measurement Database Schema (MySQL)
-- Optimized for MySQL 5.7+ and MySQL 8.0+

-- Create QUANTITY_MEASUREMENT_ENTITY table
CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation VARCHAR(50) NOT NULL,
    operand1 VARCHAR(255),
    operand2 VARCHAR(255),
    result VARCHAR(255),
    error VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_operation (operation),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_qme_operation ON quantity_measurement_entity(operation);
CREATE INDEX IF NOT EXISTS idx_qme_created_date ON quantity_measurement_entity(created_at);
