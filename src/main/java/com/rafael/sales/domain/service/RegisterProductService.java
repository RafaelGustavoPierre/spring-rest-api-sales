package com.rafael.sales.domain.service;

import com.rafael.sales.api.model.input.ProductInput;
import com.rafael.sales.domain.exception.EntityInUseException;
import com.rafael.sales.domain.exception.EntityNotFoundException;
import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.model.ProductMedia;
import com.rafael.sales.domain.repository.ProductMediaRepository;
import com.rafael.sales.domain.repository.ProductRepository;

import com.rafael.sales.domain.service.StorageService.File;

import lombok.AllArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RegisterProductService {

    public static final String PRODUCT_IN_USE = "O produto de código %d está vinculado a uma venda!";
    public static final String PRODUCT_NOT_FOUND = "O produto de código %d não foi encontrado!";

    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;

    private final StorageService storageService;


    @Transactional
    public Product save(Product product, ProductInput productInput) throws IOException {
        MultipartFile file = productInput.getFile();
        String fileName = storageService.generateFileName(file.getOriginalFilename());

        ProductMedia productMedia = new ProductMedia();
        productMedia.setFileName(fileName);
        productMedia.setContentType(file.getContentType());
        productMedia.setSize(file.getSize());

        productRepository.save(product);
        productMedia.setProduct(product);

        var media = productMediaRepository.save(productMedia);
        product.setProductMedia(media);


        File fileInfo = File.builder()
                .fileName(media.getFileName())
                .contentType(media.getContentType())
                .size(media.getSize())
                .inputStream(productInput.getFile().getInputStream())
                .build();

        storageService.save(fileInfo);

        return product;
    }

    @Transactional
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public void exclude(Long productId) {
        try {
            var existsProduct = productRepository.existsById(productId);
            if (!existsProduct)
                throw new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, productId));

            productRepository.deleteById(productId);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(PRODUCT_IN_USE, productId));
        }
    }

    private String generateFileName(String fileName) {
        return UUID.randomUUID().toString() + "_" + fileName;
    }

}
