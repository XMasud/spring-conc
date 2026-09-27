package com.example.spring_conc.service;

import com.example.spring_conc.dto.requests.ProductRequest;
import com.example.spring_conc.dto.responses.ProductResponse;
import com.example.spring_conc.entity.Product;
import com.example.spring_conc.exception.ProductNotFoundException;
import com.example.spring_conc.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
                        new BigDecimal("1200"),
                        10
                );

        Product savedProduct = Product.builder()
                .name(request.name())
                .price(request.price())
                .quantity(request.quantity())
                .build();

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

    @Test
    public void shouldNotReturnProduct() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProduct(1L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found: 1");
    }

    @Test
    public void shouldReturnProduct() {
        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(BigDecimal.valueOf(1200))
                .quantity(1)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse result = productService.getProduct(1L);

        assertThat(result.name()).isEqualTo(product.getName());
        assertThat(result.price()).isEqualTo(product.getPrice());
    }
}