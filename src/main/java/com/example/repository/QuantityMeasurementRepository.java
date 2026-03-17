package com.example.repository;
import com.example.entity.QuantityMeasurementEntity;
import com.example.exception.DatabaseException;
import java.util.List;

public interface QuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity) throws DatabaseException;

    List<QuantityMeasurementEntity> findAll() throws DatabaseException;
}