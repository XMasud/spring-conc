package com.example.spring_conc.mapper;

import com.example.spring_conc.dto.requests.ProductRequest;
import com.example.spring_conc.dto.responses.ProductResponse;
import com.example.spring_conc.entity.Product;

import java.math.BigDecimal;

public class ProductMapper {

    public static Product toEntity(ProductRequest request) {

        return Product.builder()
                .name(request.name())
                .price(request.price())
                .quantity(request.quantity())
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity()
        );
    }
}
