package com.example.spring_conc.controller;

import com.example.spring_conc.dto.requests.ProductRequest;
import com.example.spring_conc.dto.responses.ProductResponse;
import com.example.spring_conc.entity.Product;
import com.example.spring_conc.exception.NotFoundException;
import com.example.spring_conc.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequest request;
    private ProductResponse response;
    private Product product;

    @BeforeEach
    void setUp() {

        request = new ProductRequest(
                "Product-1",
                BigDecimal.valueOf(100),
                20
        );

        response = new ProductResponse(
                1L,
                "Product-1",
                BigDecimal.valueOf(100),
                20
        );

        product = Product.builder()
                .id(1L)
                .name("Product-1")
                .price(BigDecimal.valueOf(100))
                .quantity(20)
                .build();
    }

    @Test
    void shouldCreateProduct() throws Exception {

        when(productService.createProduct(request)).thenReturn(response);

        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.price").value(request.price()))
                .andExpect(jsonPath("$.quantity").value(request.quantity()));

        verify(productService).createProduct(request);
    }

    @Test
    void shouldFindProductById() throws Exception {

        when(productService.getProduct(1L))
                .thenReturn(product);

        mockMvc.perform(get("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.quantity").value(product.getQuantity()));

        verify(productService).getProduct(1L);
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {

        when(productService.getProduct(1L))
                .thenThrow(new NotFoundException("Product not found: 1"));

        mockMvc.perform(get("/api/products/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService).getProduct(1L);
    }

    @Test
    void shouldUpdateProduct() throws Exception {

        when(productService.updateProduct(1L, request)).thenReturn(response);

        mockMvc.perform(put("/api/products/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.price").value(request.price()))
                .andExpect(jsonPath("$.quantity").value(request.quantity()));

        verify(productService).updateProduct(1L, request);
    }

    @Test
    void shouldDeleteProduct() throws Exception {

        mockMvc.perform(delete("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }
}