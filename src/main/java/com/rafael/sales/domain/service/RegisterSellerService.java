package com.rafael.sales.domain.service;

import com.rafael.sales.domain.exception.EntityNotFoundException;
import com.rafael.sales.domain.model.Seller;
import com.rafael.sales.domain.repository.SellerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RegisterSellerService {

    private static final String SELLER_NOT_FOUND = "Vendedor de código %s não foi encontrado.";

    private SellerRepository sellerRepository;

    public Seller findSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId).orElseThrow(() -> new EntityNotFoundException(String.format(SELLER_NOT_FOUND, sellerId)));
    }

}
