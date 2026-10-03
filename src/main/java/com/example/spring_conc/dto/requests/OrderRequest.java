package com.example.spring_conc.dto.requests;

import java.util.List;

public record OrderRequest(
        List<OrderItemRequest> orderItems
) {
}
