package com.example.spring_conc.service;

import com.example.spring_conc.dto.requests.ProductRequest;
import com.example.spring_conc.dto.responses.ProductResponse;
import com.example.spring_conc.entity.Product;
import com.example.spring_conc.exception.NotFoundException;
import com.example.spring_conc.mapper.ProductMapper;
import com.example.spring_conc.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {

        Product savedProduct = productRepository.save(ProductMapper.toEntity(request));

        return ProductMapper.toResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    public Product getProduct(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Product not found: " + id));
    }

    @Transactional
    public ProductResponse updateProduct(
            Long id,
            ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Product not found: " + id));

        product.updateProduct(request.name(), request.price(), request.quantity());

        return ProductMapper.toResponse(product);
    }

    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Product not found: " + id));

        productRepository.delete(product);
    }
}
