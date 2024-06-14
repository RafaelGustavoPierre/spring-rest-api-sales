package com.rafael.sales.api.controller;

import com.rafael.sales.domain.model.Sale;
import com.rafael.sales.domain.repository.SaleRepository;
import com.rafael.sales.domain.service.RegisterSaleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleRepository saleRepository;
    private final RegisterSaleService registerSaleService;

    @GetMapping
    public List<Sale> list() {
        return saleRepository.findTop20ByOrderByIdDesc();
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<Sale> find(@PathVariable Long saleId) {
        return saleRepository.findById(saleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sale create(@Valid @RequestBody Sale sale) {
        return registerSaleService.save(sale);
    }

    @PutMapping("/{saleId}")
    public ResponseEntity<Sale> edit(@PathVariable Long saleId, @RequestBody @Valid Sale sale) {
        if (!saleRepository.existsById(saleId)) {
            return ResponseEntity.notFound().build();
        }

        sale.setId(saleId);
        registerSaleService.edit(sale);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    public Sale cancel(@RequestBody Sale sale) {
        return registerSaleService.cancel(sale);
    }

}
