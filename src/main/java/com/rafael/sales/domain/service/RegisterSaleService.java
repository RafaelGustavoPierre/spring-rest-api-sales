package com.rafael.sales.domain.service;

import com.rafael.sales.domain.model.Sale;
import com.rafael.sales.domain.repository.SaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RegisterSaleService {

    private final SaleRepository saleRepository;

    @Transactional
    public Sale save(Sale sale) {
        sale.setDateRegister(OffsetDateTime.now());

        return saleRepository.save(sale);
    }

}
