package com.example.spring_conc.service;

import com.example.spring_conc.dto.requests.ProductRequest;
import com.example.spring_conc.dto.responses.ProductResponse;
import com.example.spring_conc.entity.Product;
import com.example.spring_conc.exception.NotFoundException;
import com.example.spring_conc.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void shouldReturnProduct() {

        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(BigDecimal.valueOf(1200))
                .quantity(1)
                .build();

        when(productRepository.findById(1L)).
                thenReturn(Optional.of(product));

        ProductResponse productResponse = productService.getProduct(product.getId());

        assertThat(productResponse.id()).isEqualTo(product.getId());
        assertThat(productResponse.name()).isEqualTo(product.getName());
        assertThat(productResponse.price()).isEqualTo(product.getPrice());
        assertThat(productResponse.quantity()).isEqualTo(product.getQuantity());
    }

    @Test
    void shouldNotReturnProduct() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProduct(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Product not found: 1");
    }


    @Test
    void shouldUpdateProduct() {

        long productId = 1L;


        Product product = Product.builder()
                .name("Laptop")
                .price(BigDecimal.valueOf(1000))
                .quantity(10)
                .build();

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        ProductRequest updateRequest = new ProductRequest(
                "Laptop",
                BigDecimal.valueOf(1000),
                10
        );

        ProductResponse response = productService.updateProduct(productId, updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.price())
                .isEqualByComparingTo(BigDecimal.valueOf(1000));

        verify(productRepository, times(1))
                .findById(productId);
    }

    @Test
    void shouldNotUpdateProduct(){

        long productId = 1L;

        ProductRequest updateRequest = new ProductRequest(
                "Samsung Tab",
                BigDecimal.valueOf(1000),
                10
        );

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> productService.updateProduct(productId, updateRequest))
                .hasMessage("Product not found: 1")
                .isInstanceOf(NotFoundException.class);

    }


    @Test
    void shouldDeleteProduct() {


        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(BigDecimal.valueOf(1200))
                .quantity(1)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void shouldNotDeleteProductNotExist() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProduct(1L)).
                isInstanceOf(NotFoundException.class).
                hasMessage("Product not found: 1");

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).delete(any(Product.class));
    }
}