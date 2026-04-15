package com.example.measurement_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConversionRequestDTO {
    @NotNull(message = "value is required")
    private Double value;
    @NotBlank(message = "sourceUnit is required")
    private String sourceUnit;
    @NotBlank(message = "targetUnit is required")
    private String targetUnit;
    @NotBlank(message = "measurementType is required")
    private String measurementType;
}