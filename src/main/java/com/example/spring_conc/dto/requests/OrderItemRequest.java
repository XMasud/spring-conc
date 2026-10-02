package com.example.spring_conc.dto.requests;

public record OrderItemRequest(
        Long productId,
        Integer quantity
) {
}
