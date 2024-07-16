package com.rafael.sales.api.controller;

import com.rafael.sales.api.assembler.ProductMediaModelDisassembler;
import com.rafael.sales.api.assembler.ProductModelAssembler;
import com.rafael.sales.api.assembler.ProductModelDisassembler;
import com.rafael.sales.api.model.ProductModel;
import com.rafael.sales.api.model.input.ProductInput;
import com.rafael.sales.api.model.input.ProductMediaInput;
import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.model.ProductMedia;
import com.rafael.sales.domain.repository.ProductMediaRepository;
import com.rafael.sales.domain.repository.ProductRepository;
import com.rafael.sales.domain.service.RegisterProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final RegisterProductService registerProductService;
    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;

    private final ProductModelAssembler productModelAssembler;
    private final ProductModelDisassembler productModelDisassembler;

    @GetMapping
    public List<ProductModel> list() {
        return productModelAssembler.toCollectionModel(productRepository.findAll());
    }

    @GetMapping("/{productId}")
    public ProductModel find(@PathVariable Long productId) {
        return productModelAssembler.toModel(registerProductService.findProductById(productId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductModel send(@Valid ProductInput productInput)  {
        Product product = productModelDisassembler.toDomainObject(productInput);

        return productModelAssembler.toModel(registerProductService.save(product, productInput));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductModel> update(@PathVariable Long id, @Valid @RequestBody ProductInput productInput) {
        Product productRegistred = registerProductService.findProductById(id);

        productRegistred.setName(productInput.getName());
        productRegistred.setQuantity(productInput.getQuantity().add(productRegistred.getQuantity()));

//        registerProductService.save(productRegistred);
        return ResponseEntity.ok(productModelAssembler.toModel(productRegistred));
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void exclude(@PathVariable Long productId) {
        registerProductService.exclude(productId);
    }

}