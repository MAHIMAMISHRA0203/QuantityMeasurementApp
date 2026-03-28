package com.example.quantity_measurement_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Response DTO for Operation Results
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationResponseDTO {
    private Long id;
    private String operation;
    private String operand1;
    private String operand2;
    private String result;
    private Integer status; // 0 = Success, 1 = Error
    private String error;
    private LocalDateTime createdAt;

    public static OperationResponseDTO fromEntity(com.example.quantity_measurement_app.entity.QuantityMeasurementEntity entity) {
        return OperationResponseDTO.builder()
                .id(entity.getId())
                .operation(entity.getOperation())
                .operand1(entity.getOperand1())
                .operand2(entity.getOperand2())
                .result(entity.getResult())
                .status(entity.hasError() ? 1 : 0)
                .error(entity.getError())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
