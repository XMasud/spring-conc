package com.example.spring_conc.service;

import com.example.spring_conc.dto.requests.OrderItemRequest;
import com.example.spring_conc.dto.requests.OrderRequest;
import com.example.spring_conc.entity.Order;
import com.example.spring_conc.entity.Product;
import com.example.spring_conc.entity.enums.OrderStatus;
import com.example.spring_conc.exception.InvalidStatusException;
import com.example.spring_conc.exception.NotFoundException;
import com.example.spring_conc.repository.OrderItemRepository;
import com.example.spring_conc.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductService productService;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderService underTest;

    @Test
    void shouldReturnOrderById() {
        // arrange
        Long id = 1L;

        Order orderResponse = Order.builder()
                .id(id)
                .build();

        when(orderRepository.findById(id)).thenReturn(Optional.of(orderResponse));

        // act
        Order order = underTest.getOrderById(id);

        // assert
        assertNotNull(order);
        assertEquals(orderResponse.getId(), order.getId());
    }

    @Test
    void shouldNotReturnOrderWhenOrderDoesNotExists() {
        // arrange

        // act
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> underTest.getOrderById(1L));
        assertEquals("Order not found with id: 1", exception.getMessage());
    }

    @Test
    void shouldReturnAllOrders() {
        // arrange
        List<Order> orders = List.of(
                Order.builder()
                        .id(1L)
                        .build(),
                Order.builder()
                        .id(2L)
                        .build()
        );

        when(orderRepository.findAll()).thenReturn(orders);

        // act
        List<Order> result = underTest.getAllOrders();

        // assert
        assertEquals(orders, result);
        assertEquals(orders.size(), result.size());
    }

    @Test
    void shouldCreateOrder() {
        // arrange
        OrderRequest orderRequest = new OrderRequest(
                List.of(
                        new OrderItemRequest(1L, 2),
                        new OrderItemRequest(2L, 3)
                )
        );

        Product product1 = Product.builder()
                .id(1L)
                .price(BigDecimal.valueOf(10))
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .price(BigDecimal.valueOf(5))
                .build();

        Order order = Order.builder()
                .id(1L)
                .totalAmount(BigDecimal.valueOf(35))
                .status(OrderStatus.PENDING)
                .build();

        when(productService.getProduct(1L)).thenReturn(product1);
        when(productService.getProduct(2L)).thenReturn(product2);

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        //doNothing().when(orderItemService).createOrderItem(any(), any(), any());


        // act
        Order savedOrder = underTest.createOrder(orderRequest);

        // assert
        assertNotNull(savedOrder);
        assertEquals(BigDecimal.valueOf(35), savedOrder.getTotalAmount());
        assertEquals(OrderStatus.PENDING, savedOrder.getStatus());
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void shouldChangeOrderStatus() {
        // arrange
        Long id = 1L;

        Order orderResponse = Order.builder()
                .id(id)
                .status(OrderStatus.PENDING)
                .build();

        when(orderRepository.findById(id)).thenReturn(Optional.of(orderResponse));
        when(orderRepository.save(any(Order.class))).thenReturn(orderResponse);

        // act
        Order updatedOrder = underTest.changeOrderStatus(id, OrderStatus.PROCESSING);

        // assert
        assertEquals(OrderStatus.PROCESSING, updatedOrder.getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldNotChangeStatusWhenStatusIsInvalid() {
        // arrange
        Long id = 1L;

        Order orderResponse = Order.builder()
                .id(id)
                .status(OrderStatus.PENDING)
                .build();

        when(orderRepository.findById(id)).thenReturn(Optional.of(orderResponse));

        // act & assert
        assertThrows(InvalidStatusException.class, () -> underTest.changeOrderStatus(id, OrderStatus.DELIVERED));
    }

}
