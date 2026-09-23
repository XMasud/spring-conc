package com.example.spring_conc.controller;

import com.example.spring_conc.dto.requests.ProductRequest;
import com.example.spring_conc.dto.responses.APIResponse;
import com.example.spring_conc.dto.responses.ProductResponse;
import com.example.spring_conc.entity.Product;
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
    public APIResponse<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse product = productService.createProduct(request);

        return new APIResponse<>(
                true,
                "Product created successfully",
                product
        );
    }

    @GetMapping
    public APIResponse<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> products = productService.getAllProducts();

        return new APIResponse<>(
                true,
                "Products retrieved successfully",
                products
        );
    }

    @GetMapping("/{id}")
    public APIResponse<ProductResponse> getProduct(
            @PathVariable Long id) {

        ProductResponse product = productService.getProduct(id);

        return new APIResponse<>(
                true,
                "Product retrieved successfully",
                product
        );
    }

    @PutMapping("/{id}")
    public APIResponse<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse product =
                productService.updateProduct(id, request);

        return new APIResponse<>(
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
