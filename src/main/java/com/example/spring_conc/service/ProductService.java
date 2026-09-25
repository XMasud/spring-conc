package com.example.spring_conc.service;

import com.example.spring_conc.dto.requests.ProductRequest;
import com.example.spring_conc.dto.responses.ProductResponse;
import com.example.spring_conc.entity.Product;
import com.example.spring_conc.exception.ProductNotFoundException;
import com.example.spring_conc.mapper.ProductMapper;
import com.example.spring_conc.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.spring_conc.mapper.ProductMapper.toResponse;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {

        Product savedProduct = productRepository.save(ProductMapper.toEntity(request));

        return toResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    public ProductResponse getProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found: "+ id));

        return ProductMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(
            Long id,
            ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found: "+ id));

        product.setName(request.name());
        product.setPrice(request.price());
        product.setQty(request.qty());

        //Product updateProduct = productRepository.save(product);

        return ProductMapper.toResponse(product);
    }

    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                        .orElseThrow(()-> new ProductNotFoundException("Product not found: "+ id));

        productRepository.deleteById(id);
    }

}
