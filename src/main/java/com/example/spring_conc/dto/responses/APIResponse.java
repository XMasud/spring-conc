package com.example.spring_conc.dto.responses;


public record APIResponse<T>(
        boolean success,
        String message,
        T data
) {
}
