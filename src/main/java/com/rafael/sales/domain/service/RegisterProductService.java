package com.rafael.sales.domain.service;

import com.rafael.sales.api.model.input.ProductInput;
import com.rafael.sales.domain.exception.EntityInUseException;
import com.rafael.sales.domain.exception.EntityNotFoundException;
import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.model.ProductMedia;
import com.rafael.sales.domain.repository.ProductMediaRepository;
import com.rafael.sales.domain.repository.ProductRepository;
import com.rafael.sales.domain.service.StorageService.File;
import com.rafael.sales.domain.service.StorageService.MediaRecover;

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

    public static final String PRODUCT_IN_USE = "O produto de código %d está vinculado a uma venda, e não pode ser excluido.";
    public static final String PRODUCT_NOT_FOUND = "O produto de código %d não foi encontrado!";

    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;

    private final StorageService storageService;

    @Transactional
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Transactional
    public Product save(Product product, ProductInput productInput) throws IOException {
        MultipartFile file = productInput.getFile();
        String fileName = storageService.generateFileName(file.getOriginalFilename());

        ProductMedia productMedia = mountProductMedia(product, productInput);

        productRepository.save(product);
        productMedia.setProduct(product);

        var media = productMediaRepository.save(productMedia);
        product.setProductMedia(media);

        File fileInfo = mountFile(productInput, media);

        storageService.save(fileInfo);
        return product;
    }

    @Transactional
    public Product updateProduct(Product product, ProductInput productInput) throws IOException {
        storageService.removeS3(product.getProductMedia().getFileName());
        productMediaRepository.delete(product.getProductMedia());

        ProductMedia productMedia = mountProductMedia(product, productInput);

        var media = productMediaRepository.save(productMedia);

        product.setProductMedia(media);
        product.setQuantity(productInput.getQuantity());
        product.setName(product.getName());

        File fileInfo = mountFile(productInput, product.getProductMedia());

        storageService.save(fileInfo);

        return product;
    }

    public MediaRecover recover(String fileName) {
        return storageService.recorver(fileName);
    }

    public void exclude(Long productId) {
        try {
            Product product = this.findProductById(productId);

            productRepository.deleteById(productId);
            storageService.removeS3(product.getProductMedia().getFileName());
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(PRODUCT_IN_USE, productId));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, productId));
        }
    }

    private static File mountFile(ProductInput productInput, ProductMedia media) throws IOException {
        File fileInfo = File.builder()
                .fileName(media.getFileName())
                .contentType(media.getContentType())
                .size(media.getSize())
                .inputStream(productInput.getFile().getInputStream())
                .build();
        return fileInfo;
    }

    private static ProductMedia mountProductMedia(Product product, ProductInput productInput) {
        String fileName = generateFileName(productInput.getFile().getOriginalFilename());
        ProductMedia productMedia = new ProductMedia();
        productMedia.setFileName(fileName);
        productMedia.setContentType(productInput.getFile().getContentType());
        productMedia.setSize(productInput.getFile().getSize());
        productMedia.setProduct(product);
        return productMedia;
    }

    private static String generateFileName(String fileName) {
        return UUID.randomUUID().toString() + "_" + fileName;
    }

}
