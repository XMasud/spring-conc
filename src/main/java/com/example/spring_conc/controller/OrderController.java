package com.example.spring_conc.controller;

import com.example.spring_conc.dto.requests.OrderRequest;
import com.example.spring_conc.dto.responses.OrderResponse;
import com.example.spring_conc.entity.Order;
import com.example.spring_conc.mapper.OrderMapper;
import com.example.spring_conc.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {

        Order order = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderMapper.toDTO(order));
    }
}
