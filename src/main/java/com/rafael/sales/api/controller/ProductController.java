package com.rafael.sales.api.controller;

import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.repository.ProductRepository;
import com.rafael.sales.domain.service.RegisterProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.ToString;
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
    public ResponseEntity<Product> find(@PathVariable Long productId) {
        return productRepository.findById(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product send(@Valid @RequestBody Product product) {
        return registerProductService.save(product);
    }

    @PutMapping
    public ResponseEntity<Product> update(@Valid @RequestBody Product product) {
        if (!productRepository.existsById(product.getId())) {
            return ResponseEntity.notFound().build();
        }

        var productRegistred = productRepository.findById(product.getId());
        product.setQuantity(productRegistred.get().getQuantity().add(product.getQuantity()));

        Product productUpdated = registerProductService.save(product);
        return ResponseEntity.ok(productUpdated);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> exclude(@PathVariable Long productId) {
        if (!productRepository.existsById(productId)) {
            return ResponseEntity.notFound().build();
        }

        registerProductService.exclude(productId);
        return ResponseEntity.noContent().build();
    }

}