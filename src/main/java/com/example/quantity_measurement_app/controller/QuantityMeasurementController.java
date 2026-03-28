package com.example.quantity_measurement_app.controller;

import com.example.quantity_measurement_app.dto.*;
import com.example.quantity_measurement_app.entity.QuantityMeasurementEntity;
import com.example.quantity_measurement_app.exception.QuantityMeasurementException;
import com.example.quantity_measurement_app.repository.QuantityMeasurementOperationRepository;
import com.example.quantity_measurement_app.service.IQuantityMeasurementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
@RequiredArgsConstructor
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;
    private final QuantityMeasurementOperationRepository repository;

         @PostMapping("/compare")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> compare(
            @Valid @RequestBody ComparisonRequestDTO request) {
        try {
            QuantityDTO q1 = new QuantityDTO(
                    request.getValue1(),
                    request.getUnit1(),
                    request.getMeasurementType()
            );
            QuantityDTO q2 = new QuantityDTO(
                    request.getValue2(),
                    request.getUnit2(),
                    request.getMeasurementType()
            );

            boolean result = service.compare(q1, q2);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("q1", q1.getValue() + " " + q1.getUnit());
            responseData.put("q2", q2.getValue() + " " + q2.getUnit());
            responseData.put("equal", result);

            return ResponseEntity.ok(ApiResponseDTO.success(responseData,
                    "Comparison completed: " + q1.getValue() + " " + q1.getUnit() +
                    (result ? " equals " : " does not equal ") + q2.getValue() + " " + q2.getUnit()));
        } catch (QuantityMeasurementException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(400, e.getMessage()));
        }
    }

    //CONVERSION 

        @PostMapping("/convert")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> convert(
            @Valid @RequestBody ConversionRequestDTO request) {
        try {
            QuantityDTO source = new QuantityDTO(
                    request.getValue(),
                    request.getSourceUnit(),
                    request.getMeasurementType()
            );

            QuantityDTO result = service.convert(source, request.getTargetUnit());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("source", source.getValue() + " " + source.getUnit());
            responseData.put("result", result.getValue() + " " + result.getUnit());

            return ResponseEntity.ok(ApiResponseDTO.success(responseData,
                    "Conversion completed: " + source.getValue() + " " + source.getUnit() +
                    " = " + result.getValue() + " " + result.getUnit()));
        } catch (QuantityMeasurementException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(400, e.getMessage()));
        }
    }

    //ADDITION

    @PostMapping("/add")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> add(
            @Valid @RequestBody ArithmeticOperationRequestDTO request) {
        try {
            QuantityDTO q1 = new QuantityDTO(
                    request.getValue1(),
                    request.getUnit1(),
                    request.getMeasurementType()
            );
            QuantityDTO q2 = new QuantityDTO(
                    request.getValue2(),
                    request.getUnit2(),
                    request.getMeasurementType()
            );

            QuantityDTO result = service.add(q1, q2);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("operand1", q1.getValue() + " " + q1.getUnit());
            responseData.put("operand2", q2.getValue() + " " + q2.getUnit());
            responseData.put("result", result.getValue() + " " + result.getUnit());

            return ResponseEntity.ok(ApiResponseDTO.success(responseData,
                    "Addition completed: " + q1.getValue() + " " + q1.getUnit() +
                    " + " + q2.getValue() + " " + q2.getUnit() +
                    " = " + result.getValue() + " " + result.getUnit()));
        } catch (QuantityMeasurementException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(400, e.getMessage()));
        }
    }

    //SUBTRACTION 
    @PostMapping("/subtract")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> subtract(
            @Valid @RequestBody ArithmeticOperationRequestDTO request) {
        try {
            QuantityDTO q1 = new QuantityDTO(
                    request.getValue1(),
                    request.getUnit1(),
                    request.getMeasurementType()
            );
            QuantityDTO q2 = new QuantityDTO(
                    request.getValue2(),
                    request.getUnit2(),
                    request.getMeasurementType()
            );

            QuantityDTO result = service.subtract(q1, q2);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("operand1", q1.getValue() + " " + q1.getUnit());
            responseData.put("operand2", q2.getValue() + " " + q2.getUnit());
            responseData.put("result", result.getValue() + " " + result.getUnit());

            return ResponseEntity.ok(ApiResponseDTO.success(responseData,
                    "Subtraction completed: " + q1.getValue() + " " + q1.getUnit() +
                    " - " + q2.getValue() + " " + q2.getUnit() +
                    " = " + result.getValue() + " " + result.getUnit()));
        } catch (QuantityMeasurementException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(400, e.getMessage()));
        }
    }
    //  DIVISION

      @PostMapping("/divide")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> divide(
            @Valid @RequestBody ArithmeticOperationRequestDTO request) {
        try {
            QuantityDTO q1 = new QuantityDTO(
                    request.getValue1(),
                    request.getUnit1(),
                    request.getMeasurementType()
            );
            QuantityDTO q2 = new QuantityDTO(
                    request.getValue2(),
                    request.getUnit2(),
                    request.getMeasurementType()
            );

            double result = service.divide(q1, q2);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("operand1", q1.getValue() + " " + q1.getUnit());
            responseData.put("operand2", q2.getValue() + " " + q2.getUnit());
            responseData.put("result", result);

            return ResponseEntity.ok(ApiResponseDTO.success(responseData,
                    "Division completed: " + q1.getValue() + " " + q1.getUnit() +
                    " / " + q2.getValue() + " " + q2.getUnit() +
                    " = " + result));
        } catch (QuantityMeasurementException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error(400, e.getMessage()));
        }
    }

    //OPERATION HISTORY 

   
    @GetMapping("/history")
    public ResponseEntity<ApiResponseDTO<List<OperationResponseDTO>>> getHistory() {
        List<QuantityMeasurementEntity> operations = repository.findAll();
        List<OperationResponseDTO> responseList = operations.stream()
                .map(OperationResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseDTO.success(responseList,
                "Retrieved " + responseList.size() + " operations"));
    }

    @GetMapping("/history/by-type")
    public ResponseEntity<ApiResponseDTO<List<OperationResponseDTO>>> getHistoryByType(
            @RequestParam String type) {
        List<QuantityMeasurementEntity> operations = repository.findByOperation(type);
        List<OperationResponseDTO> responseList = operations.stream()
                .map(OperationResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseDTO.success(responseList,
                "Retrieved " + responseList.size() + " " + type + " operations"));
    }

    @GetMapping("/history/successful")
    public ResponseEntity<ApiResponseDTO<List<OperationResponseDTO>>> getSuccessfulOperations() {
        List<QuantityMeasurementEntity> operations = repository.findAllSuccessful();
        List<OperationResponseDTO> responseList = operations.stream()
                .map(OperationResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseDTO.success(responseList,
                "Retrieved " + responseList.size() + " successful operations"));
    }

   
    @GetMapping("/history/errors")
    public ResponseEntity<ApiResponseDTO<List<OperationResponseDTO>>> getErrorOperations() {
        List<QuantityMeasurementEntity> operations = repository.findAllErrors();
        List<OperationResponseDTO> responseList = operations.stream()
                .map(OperationResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseDTO.success(responseList,
                "Retrieved " + responseList.size() + " error operations"));
    }

    
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOperations", repository.count());
        stats.put("successfulOperations", repository.countSuccessfulOperations());
        stats.put("errorOperations", repository.countErrorOperations());
        stats.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(ApiResponseDTO.success(stats, "Statistics retrieved"));
    }

    @DeleteMapping("/history")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> clearHistory() {
        long count = repository.count();
        repository.deleteAll();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("deletedCount", count);

        return ResponseEntity.ok(ApiResponseDTO.success(responseData,
                "Deleted " + count + " operations from history"));
    }

    // LEGACY METHODS (Used by tests) 

    public boolean performComparison(QuantityDTO q1, QuantityDTO q2)
            throws QuantityMeasurementException {
        return service.compare(q1, q2);
    }

    public QuantityDTO performConversion(QuantityDTO source, QuantityDTO targetUnit)
            throws QuantityMeasurementException {
        return service.convert(source, targetUnit.getUnit());
    }

    public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2)
            throws QuantityMeasurementException {
        return service.add(q1, q2);
    }

    public QuantityDTO performSubtraction(QuantityDTO q1, QuantityDTO q2)
            throws QuantityMeasurementException {
        return service.subtract(q1, q2);
    }

    public double performDivision(QuantityDTO q1, QuantityDTO q2)
            throws QuantityMeasurementException {
        return service.divide(q1, q2);
    }
}
