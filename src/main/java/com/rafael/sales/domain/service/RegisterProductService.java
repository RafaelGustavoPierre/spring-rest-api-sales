package com.rafael.sales.domain.service;

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

    public void exclude(Long productId) {
        productRepository.deleteById(productId);
    }

}
