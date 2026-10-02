package com.example.spring_conc.service;

import com.example.spring_conc.entity.Order;
import com.example.spring_conc.entity.OrderItem;
import com.example.spring_conc.entity.Product;
import com.example.spring_conc.repository.OrderItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {
    @Mock
    OrderItemRepository orderItemRepository;

    @InjectMocks
    OrderItemService underTest;

    @Test
    void shouldCreateOrderItem() {
        // arrange
        Integer quantity = 1;

        Order order = Order.builder()
                .id(1L)
                .build();

        Product product = Product.builder()
                .id(1L)
                .build();

        // act
        underTest.createOrderItem(quantity, order, product);

        // assert
        ArgumentCaptor<OrderItem> orderItemArgumentCaptor = ArgumentCaptor.forClass(OrderItem.class);
        verify(orderItemRepository).save(orderItemArgumentCaptor.capture());
        OrderItem orderItem = orderItemArgumentCaptor.getValue();

        assertNotNull(orderItem);
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(order, orderItem.getOrder());
        assertEquals(product, orderItem.getProduct());
    }

}