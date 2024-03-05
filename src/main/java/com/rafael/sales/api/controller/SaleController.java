package com.rafael.sales.api.controller;

import com.rafael.sales.domain.model.Sale;
import com.rafael.sales.domain.service.RegisterSaleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("/sales")
public class SaleController {

    private final RegisterSaleService registerSaleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sale create(@Valid @RequestBody Sale sale) {
        return registerSaleService.save(sale);
    }

}
