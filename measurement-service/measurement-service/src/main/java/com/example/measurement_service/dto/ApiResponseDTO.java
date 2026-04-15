package com.example.measurement_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private String error;
    private T data;

    public static <T> ApiResponseDTO<T> success(T data, String message) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .statusCode(200)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDTO<T> error(int statusCode, String message) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .statusCode(statusCode)
                .error(message)
                .build();
    }
}
