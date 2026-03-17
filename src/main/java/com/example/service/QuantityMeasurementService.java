package com.example.service;

import com.example.dto.QuantityDTO;
import com.example.exception.QuantityMeasurementException;
import com.example.service.QuantityMeasurementService;
public interface QuantityMeasurementService {

    boolean compare(QuantityDTO q1, QuantityDTO q2);

    QuantityDTO convert(QuantityDTO source, String targetUnit);

    QuantityDTO add(QuantityDTO q1, QuantityDTO q2);

    QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2);

    double divide(QuantityDTO q1, QuantityDTO q2);
}