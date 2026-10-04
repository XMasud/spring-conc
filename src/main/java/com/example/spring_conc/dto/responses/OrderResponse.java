package com.example.spring_conc.dto.responses;

import com.example.spring_conc.entity.enums.OrderStatus;

import java.math.BigDecimal;

public record OrderResponse(
        Long id,
        BigDecimal totalAmount,
        OrderStatus status
) {
}
