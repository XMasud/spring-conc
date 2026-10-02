package com.example.spring_conc.service;

import com.example.spring_conc.entity.Order;
import com.example.spring_conc.entity.OrderItem;
import com.example.spring_conc.entity.Product;
import com.example.spring_conc.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderItemService {
    private OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public void createOrderItem(Integer quantity, Order order, Product product) {
        OrderItem orderItem = OrderItem.builder()
                .quantity(quantity)
                .order(order)
                .product(product)
                .build();

        orderItemRepository.save(orderItem);
    }
}
