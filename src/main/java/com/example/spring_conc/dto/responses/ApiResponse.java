package com.example.spring_conc.dto.responses;


public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
}
