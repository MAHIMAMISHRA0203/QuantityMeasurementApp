package com.example.measurement_service.dto.history;

import com.example.measurement_service.entity.MeasurementOperationEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OperationHistoryDTO {
    private Long id;
    private String operation;
    private String operand1;
    private String operand2;
    private String result;
    private String error;
    private Integer status;
    private LocalDateTime createdAt;

    public static OperationHistoryDTO fromEntity(MeasurementOperationEntity entity) {
        return OperationHistoryDTO.builder()
                .id(entity.getId())
                .operation(entity.getOperation())
                .operand1(entity.getOperand1())
                .operand2(entity.getOperand2())
                .result(entity.getResult())
                .error(entity.getError())
                .status(entity.getError() == null || entity.getError().isBlank() ? 1 : 0)
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
