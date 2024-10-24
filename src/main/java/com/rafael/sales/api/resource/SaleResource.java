package com.rafael.sales.api.resource;

import com.rafael.sales.api.assembler.SaleModelAssembler;
import com.rafael.sales.api.model.SaleModel;
import com.rafael.sales.api.model.input.SaleInput;
import com.rafael.sales.domain.model.Sale;
import com.rafael.sales.domain.repository.SaleRepository;
import com.rafael.sales.domain.service.RegisterSaleService;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/sales")
public class SaleResource {

    private final SaleRepository saleRepository;
    private final RegisterSaleService registerSaleService;

    private final SaleModelAssembler saleModelAssembler;

    private final PagedResourcesAssembler<Sale> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<SaleModel> list(@PageableDefault(size = 2) Pageable pageable) {
        Page<Sale> salePage = saleRepository.findAll(pageable);

        return pagedResourcesAssembler.toModel(salePage, saleModelAssembler);
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
