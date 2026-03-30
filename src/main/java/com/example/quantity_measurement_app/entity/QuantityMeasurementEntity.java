package com.example.quantity_measurement_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * JPA Entity for storing Quantity Measurement Operations.
 * Maps to quantity_measurement_operations table in MySQL database.
 */
@Entity
@Table(name = "quantity_measurement_operations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation", nullable = false, length = 50)
    private String operation;

    @Column(name = "operand1", length = 255)
    private String operand1;

    @Column(name = "operand2", length = 255)
    private String operand2;

    @Column(name = "result", length = 255)
    private String result;

    @Column(name = "error", length = 500)
    private String error;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Constructor for successful operations
     */
    public QuantityMeasurementEntity(String operation, String operand1,
                                     String operand2, String result) {
        this.operation = operation;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }

    /**
     * Constructor for error operations
     */
    public QuantityMeasurementEntity(String operation, String error) {
        this.operation = operation;
        this.error = error;
    }

    /**
     * Check if this operation has an error
     */
    public boolean hasError() {
        return error != null && !error.isEmpty();
    }

    @Override
    public String toString() {
        if (hasError()) {
            return "QuantityMeasurementEntity{" +
                    "id=" + id +
                    ", operation='" + operation + '\'' +
                    ", error='" + error + '\'' +
                    ", createdAt=" + createdAt +
                    '}';
        }
        return "QuantityMeasurementEntity{" +
                "id=" + id +
                ", operation='" + operation + '\'' +
                ", operand1='" + operand1 + '\'' +
                ", operand2='" + operand2 + '\'' +
                ", result='" + result + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
