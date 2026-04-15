package com.example.measurement_service.controller;

import com.example.measurement_service.dto.ApiResponseDTO;
import com.example.measurement_service.dto.ArithmeticOperationRequestDTO;
import com.example.measurement_service.dto.ComparisonRequestDTO;
import com.example.measurement_service.dto.ConversionRequestDTO;
import com.example.measurement_service.dto.QuantityDTO;
import com.example.measurement_service.dto.history.OperationHistoryDTO;
import com.example.measurement_service.repository.MeasurementOperationRepository;
import com.example.measurement_service.service.MeasurementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService service;
    private final MeasurementOperationRepository measurementOperationRepository;

    @PostMapping("/compare")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> compare(
            @Valid @RequestBody ComparisonRequestDTO request) {
        QuantityDTO q1 = new QuantityDTO(
                request.getValue1(), request.getUnit1(), request.getMeasurementType());
        QuantityDTO q2 = new QuantityDTO(
                request.getValue2(), request.getUnit2(), request.getMeasurementType());

        boolean result = service.compare(q1, q2);

        Map<String, Object> response = new HashMap<>();
        response.put("q1", q1.getValue() + " " + q1.getUnit());
        response.put("q2", q2.getValue() + " " + q2.getUnit());
        response.put("equal", result);
        response.put("message", q1.getValue() + " " + q1.getUnit()
                + (result ? " equals " : " does not equal ")
                + q2.getValue() + " " + q2.getUnit());

        return ResponseEntity.ok(ApiResponseDTO.success(response, "Comparison completed"));
    }

    @PostMapping("/convert")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> convert(
            @Valid @RequestBody ConversionRequestDTO request) {
        QuantityDTO source = new QuantityDTO(
                request.getValue(), request.getSourceUnit(), request.getMeasurementType());

        QuantityDTO result = service.convert(source, request.getTargetUnit());

        Map<String, Object> response = new HashMap<>();
        response.put("source", source.getValue() + " " + source.getUnit());
        response.put("result", result.getValue() + " " + result.getUnit());

        return ResponseEntity.ok(ApiResponseDTO.success(response, "Conversion completed"));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> add(
            @Valid @RequestBody ArithmeticOperationRequestDTO request) {
        QuantityDTO q1 = new QuantityDTO(
                request.getValue1(), request.getUnit1(), request.getMeasurementType());
        QuantityDTO q2 = new QuantityDTO(
                request.getValue2(), request.getUnit2(), request.getMeasurementType());

        QuantityDTO result = service.add(q1, q2);

        Map<String, Object> response = new HashMap<>();
        response.put("operand1", q1.getValue() + " " + q1.getUnit());
        response.put("operand2", q2.getValue() + " " + q2.getUnit());
        response.put("result", result.getValue() + " " + result.getUnit());

        return ResponseEntity.ok(ApiResponseDTO.success(response, "Addition completed"));
    }

    @PostMapping("/subtract")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> subtract(
            @Valid @RequestBody ArithmeticOperationRequestDTO request) {
        QuantityDTO q1 = new QuantityDTO(
                request.getValue1(), request.getUnit1(), request.getMeasurementType());
        QuantityDTO q2 = new QuantityDTO(
                request.getValue2(), request.getUnit2(), request.getMeasurementType());

        QuantityDTO result = service.subtract(q1, q2);

        Map<String, Object> response = new HashMap<>();
        response.put("operand1", q1.getValue() + " " + q1.getUnit());
        response.put("operand2", q2.getValue() + " " + q2.getUnit());
        response.put("result", result.getValue() + " " + result.getUnit());

        return ResponseEntity.ok(ApiResponseDTO.success(response, "Subtraction completed"));
    }

    @PostMapping("/divide")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> divide(
            @Valid @RequestBody ArithmeticOperationRequestDTO request) {
        QuantityDTO q1 = new QuantityDTO(
                request.getValue1(), request.getUnit1(), request.getMeasurementType());
        QuantityDTO q2 = new QuantityDTO(
                request.getValue2(), request.getUnit2(), request.getMeasurementType());

        double result = service.divide(q1, q2);

        Map<String, Object> response = new HashMap<>();
        response.put("operand1", q1.getValue() + " " + q1.getUnit());
        response.put("operand2", q2.getValue() + " " + q2.getUnit());
        response.put("result", result);

        return ResponseEntity.ok(ApiResponseDTO.success(response, "Division completed"));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponseDTO<List<OperationHistoryDTO>>> getHistory() {
        List<OperationHistoryDTO> history = measurementOperationRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(OperationHistoryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.success(history, "History fetched"));
    }

    @GetMapping("/history/by-type")
    public ResponseEntity<ApiResponseDTO<List<OperationHistoryDTO>>> getHistoryByType(
            @RequestParam String type) {
        List<OperationHistoryDTO> history = measurementOperationRepository
                .findByOperationOrderByCreatedAtDesc(type.toUpperCase())
                .stream()
                .map(OperationHistoryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDTO.success(history, "Filtered history fetched"));
    }

    @DeleteMapping("/history")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> clearHistory() {
        long deletedCount = measurementOperationRepository.count();
        measurementOperationRepository.deleteAll();
        Map<String, Object> response = new HashMap<>();
        response.put("deletedCount", deletedCount);
        return ResponseEntity.ok(ApiResponseDTO.success(response, "History cleared"));
    }
}
