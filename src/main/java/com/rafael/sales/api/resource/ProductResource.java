package com.rafael.sales.api.resource;

import com.rafael.sales.api.assembler.ProductModelAssembler;
import com.rafael.sales.api.assembler.ProductModelDisassembler;
import com.rafael.sales.api.model.ProductModel;
import com.rafael.sales.api.model.input.ProductInput;
import com.rafael.sales.core.security.CheckSecurity;
import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.repository.ProductRepository;
import com.rafael.sales.domain.service.RegisterProductMediaService;
import com.rafael.sales.domain.service.RegisterProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.rafael.sales.domain.service.StorageService.MediaRecover;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductResource {

    private final RegisterProductService registerProductService;
    private final RegisterProductMediaService registerProductMediaService;

    private final ProductRepository productRepository;

    private final ProductModelAssembler productModelAssembler;
    private final ProductModelDisassembler productModelDisassembler;

    @GetMapping
    @CheckSecurity.Products.canRead
    public List<ProductModel> list() {
        return productModelAssembler.toCollectionModel(productRepository.findAll());
    }

    @GetMapping("/{productId}")
    @CheckSecurity.Products.canRead
    public ResponseEntity<ProductModel> find(@PathVariable Long productId) {
        ProductModel productModel = productModelAssembler.toModel(registerProductService.findProductById(productId));
        String productMediaName = registerProductMediaService.findByProductMedia(productId);

        if (productMediaName == null) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .body(productModel);
        }

        MediaRecover recover = registerProductService.recover(productMediaName);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, recover.getUrl())
                .body(productModel);
    }

    @PostMapping
    @CheckSecurity.Products.canWrite
    @ResponseStatus(HttpStatus.CREATED)
    public ProductModel send(@Valid ProductInput productInput) throws IOException {
        Product product = productModelDisassembler.toDomainObject(productInput);

        return productModelAssembler.toModel(registerProductService.save(product, productInput));
    }

    @PutMapping("/{id}")
    @CheckSecurity.Products.canWrite
    public ResponseEntity<ProductModel> update(@PathVariable Long id, @Valid ProductInput productInput) throws IOException {
        Product product = registerProductService.findProductById(id);

        registerProductService.updateProduct(product, productInput);
        return ResponseEntity.ok(productModelAssembler.toModel(product));
    }

    @DeleteMapping("/{productId}")
    @CheckSecurity.Products.canWrite
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void exclude(@PathVariable Long productId) {
        registerProductService.exclude(productId);
    }

}