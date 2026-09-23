package com.example.spring_conc.service;

import com.example.spring_conc.dto.requests.ProductRequest;
import com.example.spring_conc.dto.responses.ProductResponse;
import com.example.spring_conc.entity.Product;
import com.example.spring_conc.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct() {

        ProductRequest request =
                new ProductRequest(
                        "Laptop",
                        new BigDecimal("1200")
                );

        Product savedProduct =
                new Product(
                        1L,
                        "Laptop",
                        new BigDecimal("1200")
                );

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        ProductResponse result =
                productService.createProduct(request);

        assertEquals("Laptop", result.name());
        assertEquals(
                new BigDecimal("1200"),
                result.price()
        );

        verify(productRepository)
                .save(any(Product.class));
    }

    /*
    @Test
    void getAllProducts() {
    }

    @Test
    void getProduct() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }*/
}