package com.example.spring_conc.mapper;

import com.example.spring_conc.dto.responses.OrderResponse;
import com.example.spring_conc.entity.Order;

public class OrderMapper {

    public static OrderResponse toDTO(Order order){
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }
}
