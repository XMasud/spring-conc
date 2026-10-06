package com.example.spring_conc.controller;

import com.example.spring_conc.dto.requests.OrderRequest;
import com.example.spring_conc.dto.responses.OrderResponse;
import com.example.spring_conc.entity.Order;
import com.example.spring_conc.entity.enums.OrderStatus;
import com.example.spring_conc.mapper.OrderMapper;
import com.example.spring_conc.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        Order order = orderService.changeOrderStatus(orderId, status);
        return ResponseEntity.ok(OrderMapper.toDTO(order));
    }
}
