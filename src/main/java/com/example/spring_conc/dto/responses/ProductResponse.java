package com.example.spring_conc.dto.responses;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        Integer quantity
) {
}
