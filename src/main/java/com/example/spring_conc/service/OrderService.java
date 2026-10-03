package com.example.spring_conc.service;

import com.example.spring_conc.dto.requests.OrderItemRequest;
import com.example.spring_conc.dto.requests.OrderRequest;
import com.example.spring_conc.entity.Order;
import com.example.spring_conc.entity.OrderItem;
import com.example.spring_conc.entity.Product;
import com.example.spring_conc.entity.enums.OrderStatus;
import com.example.spring_conc.exception.NotFoundException;
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
    private final OrderItemService orderItemService;

    public OrderService(OrderRepository orderRepository, ProductService productService, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderItemService = orderItemService;
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
        Map<Long, Product> products = new HashMap<>();


        for (var orderItem : orderRequest.orderItems()) {
            Product product = productService.getProduct(orderItem.productId());
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(orderItem.quantity())));
            products.put(product.getId(), product);
        }

        Order order = new Order();
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        for (var orderItem : orderRequest.orderItems()) {
            orderItemService.createOrderItem(orderItem.quantity(), savedOrder, products.get(orderItem.productId()));
        }

        return savedOrder;
    }
}
