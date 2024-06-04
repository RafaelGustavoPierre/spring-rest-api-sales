package com.rafael.sales.api.controller;

import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.repository.ProductRepository;
import com.rafael.sales.domain.service.RegisterProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final RegisterProductService registerProductService;
    private final ProductRepository productRepository;

    @GetMapping
    public List<Product> list() {
        return productRepository.findAll();
    }

    @GetMapping("/{productId}")
    public Product find(@PathVariable Long productId) {
        return registerProductService.findProductById(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product send(@Valid @RequestBody Product product) {
        return registerProductService.save(product);
    }

    @PutMapping
    public ResponseEntity<Product> update(@Valid @RequestBody Product product) {
        var productRegistred = registerProductService.findProductById(product.getId());
        product.setQuantity(productRegistred.getQuantity().add(product.getQuantity()));

        Product productUpdated = registerProductService.save(product);
        return ResponseEntity.ok(productUpdated);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> exclude(@PathVariable Long productId) {
        registerProductService.exclude(productId);
        return ResponseEntity.noContent().build();
    }

}