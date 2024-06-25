package com.rafael.sales.api.assembler;

import com.rafael.sales.api.model.input.SaleInput;
import com.rafael.sales.domain.model.Sale;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SaleModelDisassembler {

    private ModelMapper modelMapper;

    public Sale toDomainObject(SaleInput saleInput) {
        return modelMapper.map(saleInput, Sale.class);
    }

}
