package com.rafael.sales.domain.service;

import com.rafael.sales.domain.exception.EntityNotFoundException;
import com.rafael.sales.domain.model.ProductMedia;
import com.rafael.sales.domain.repository.ProductMediaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterProductMediaService {

    private static final String MEDIA_NOT_FOUND = "Media não encontrada";

    private final ProductMediaRepository productMediaRepository;

    public String findByProductMedia(Long productId) {
        return productMediaRepository.findByProductId(productId);
    }

}
