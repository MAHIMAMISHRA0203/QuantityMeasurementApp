package com.example.quantity_measurement_app.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Request DTO for comparison operations

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComparisonRequestDTO {
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
