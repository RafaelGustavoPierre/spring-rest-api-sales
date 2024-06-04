package com.rafael.sales.domain.service;

import com.rafael.sales.domain.exception.ProductNotFoundException;
import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class RegisterProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void exclude(Long productId) {
        this.findProductById(productId);
        productRepository.deleteById(productId);
    }

}
