package com.example.spring_conc.service;

import com.example.spring_conc.dto.requests.OrderItemRequest;
import com.example.spring_conc.dto.requests.OrderRequest;
import com.example.spring_conc.entity.Order;
import com.example.spring_conc.exception.NotFoundException;
import com.example.spring_conc.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found with id: " + id));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest orderItem : orderRequest.orderItems()) {
            totalAmount.add(productService.getProduct(orderItem.productId()).getPrice());
        }

        Order order = new Order();
        order.setTotalAmount(totalAmount);
        //order.setOrderItems(orderRequest.orderItems());

        return null;
    }
}
