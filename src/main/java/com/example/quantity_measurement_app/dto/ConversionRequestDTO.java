package com.example.quantity_measurement_app.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Request DTO for conversion operations

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
