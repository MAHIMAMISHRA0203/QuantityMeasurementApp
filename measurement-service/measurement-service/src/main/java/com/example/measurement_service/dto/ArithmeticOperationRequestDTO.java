package com.example.measurement_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArithmeticOperationRequestDTO {
    @NotNull(message = "value1 is required")
    private Double value1;
    @NotBlank(message = "unit1 is required")
    private String unit1;
    @NotNull(message = "value2 is required")
    private Double value2;
    @NotBlank(message = "unit2 is required")
    private String unit2;
    @NotBlank(message = "measurementType is required")
    private String measurementType;
}