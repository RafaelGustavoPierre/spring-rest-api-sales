package com.rafael.sales.api.controller;

import com.rafael.sales.api.assembler.SaleModelAssembler;
import com.rafael.sales.api.model.SaleModel;
import com.rafael.sales.api.model.input.SaleInput;
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

    private final SaleModelAssembler saleModelAssembler;

    @GetMapping
    public List<SaleModel> list() {
        List<Sale> sale = saleRepository.findAll();
        return saleModelAssembler.toCollectionModel(sale);
    }

    @GetMapping("/{saleCode}")
    @ResponseStatus(HttpStatus.OK)
    public SaleModel find(@PathVariable String saleCode) {
        return saleModelAssembler.toModel(registerSaleService.findSale(saleCode));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleModel create(@Valid @RequestBody SaleInput saleInput) {
        return registerSaleService.save(saleInput);
    }

    @PutMapping("/{saleCode}")
    public ResponseEntity<SaleModel> edit(@PathVariable String saleCode, @RequestBody @Valid SaleInput saleInput) {
        if (!saleRepository.existsByCode(saleCode)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(registerSaleService.edit(saleCode, saleInput));
    }

    @DeleteMapping("/{saleCode}")
    @ResponseStatus(HttpStatus.OK)
    public void cancel(@PathVariable String saleCode) {
        registerSaleService.cancel(saleCode);
    }

}
