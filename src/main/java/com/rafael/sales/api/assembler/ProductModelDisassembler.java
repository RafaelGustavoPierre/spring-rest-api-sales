package com.rafael.sales.api.assembler;

import com.rafael.sales.api.model.input.ProductInput;
import com.rafael.sales.domain.model.Product;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductModelDisassembler {

    private ModelMapper modelMapper;

    public Product toDomainObject(ProductInput productInput) {
        return modelMapper.map(productInput, Product.class);
    }

    public void copyToDomainObject(ProductInput productInput, Product product) {
        modelMapper.map(product, productInput);
    }



}
