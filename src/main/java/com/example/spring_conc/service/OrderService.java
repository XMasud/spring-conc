package com.example.spring_conc.service;

import com.example.spring_conc.dto.requests.OrderItemRequest;
import com.example.spring_conc.dto.requests.OrderRequest;
import com.example.spring_conc.entity.Order;
import com.example.spring_conc.entity.OrderItem;
import com.example.spring_conc.entity.Product;
import com.example.spring_conc.entity.enums.OrderStatus;
import com.example.spring_conc.exception.InvalidStatusException;
import com.example.spring_conc.exception.NotFoundException;
import com.example.spring_conc.repository.OrderItemRepository;
import com.example.spring_conc.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, ProductService productService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderItemRepository = orderItemRepository;
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

        Order order = new Order();
        order.setTotalAmount(BigDecimal.ZERO);
        order = orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest orderItem : orderRequest.orderItems()) {

            Product product = productService.getProduct(orderItem.productId());
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(orderItem.quantity())));

            OrderItem item = OrderItem.builder()
                    .quantity(orderItem.quantity())
                    .order(order)
                    .product(product)
                    .build();

            orderItemRepository.save(item);
        }

        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);

        if (!order.getStatus().isValidStatus(status)) {
            throw new InvalidStatusException("Invalid status transition");
        }

        order.setStatus(status);

        return orderRepository.save(order);
    }
}
