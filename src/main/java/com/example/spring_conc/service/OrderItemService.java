package com.example.spring_conc.service;

import com.example.spring_conc.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    private OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void createOrderItems(Object any, Object any1, Object any2) {

    }
}
