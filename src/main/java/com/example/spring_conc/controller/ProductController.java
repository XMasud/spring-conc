package com.example.spring_conc.controller;

import com.example.spring_conc.dto.requests.ProductRequest;
import com.example.spring_conc.dto.responses.ApiResponse;
import com.example.spring_conc.dto.responses.ProductResponse;
import com.example.spring_conc.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse product = productService.createProduct(request);

        return new ApiResponse<>(
                true,
                "Product created successfully",
                product
        );
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> products = productService.getAllProducts();

        return new ApiResponse<>(
                true,
                "Products retrieved successfully",
                products
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProduct(
            @PathVariable Long id) {

        ProductResponse product = productService.getProduct(id);

        return new ApiResponse<>(
                true,
                "Product retrieved successfully",
                product
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse product =
                productService.updateProduct(id, request);

        return new ApiResponse<>(
                true,
                "Product updated successfully",
                product
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
    }
}
