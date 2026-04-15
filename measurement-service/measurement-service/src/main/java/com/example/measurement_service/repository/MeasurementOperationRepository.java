package com.example.measurement_service.repository;

import com.example.measurement_service.entity.MeasurementOperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementOperationRepository extends JpaRepository<MeasurementOperationEntity, Long> {
    List<MeasurementOperationEntity> findAllByOrderByCreatedAtDesc();
    List<MeasurementOperationEntity> findByOperationOrderByCreatedAtDesc(String operation);
}
