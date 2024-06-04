package com.rafael.sales.domain.service;

import com.rafael.sales.domain.exception.EntityInUseException;
import com.rafael.sales.domain.exception.ProductNotFoundException;
import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class RegisterProductService {

    public static final String PRODUCT_IN_USE = "O produto de código %d está vinculado a uma venda!";

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
        try {
            productRepository.deleteById(productId);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(PRODUCT_IN_USE, productId));
        }
    }

}
