package com.example.quantity_measurement_app.repository;

import com.example.quantity_measurement_app.entity.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementOperationRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    /**
        * @param operation the operation type (ADDITION, CONVERSION, etc.)
     * @return list of operations matching the type
     */
    List<QuantityMeasurementEntity> findByOperation(String operation);

    @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.error IS NOT NULL AND q.error != ''")
    List<QuantityMeasurementEntity> findAllErrors();
    @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.error IS NULL OR q.error = ''")
    List<QuantityMeasurementEntity> findAllSuccessful();

    @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.createdAt BETWEEN :startDate AND :endDate ORDER BY q.createdAt DESC")
    List<QuantityMeasurementEntity> findOperationsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(q) FROM QuantityMeasurementEntity q WHERE q.error IS NULL OR q.error = ''")
    long countSuccessfulOperations();

    @Query("SELECT COUNT(q) FROM QuantityMeasurementEntity q WHERE q.error IS NOT NULL AND q.error != ''")
    long countErrorOperations();

    @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.operation = :operation AND ((:hasError = true AND q.error IS NOT NULL) OR (:hasError = false AND (q.error IS NULL OR q.error = '')))")
    List<QuantityMeasurementEntity> findByOperationAndErrorStatus(
            @Param("operation") String operation,
            @Param("hasError") boolean hasError
    );
}
