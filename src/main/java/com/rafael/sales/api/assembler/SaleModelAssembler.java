package com.rafael.sales.api.assembler;

import com.rafael.sales.api.model.SaleModel;
import com.rafael.sales.domain.model.Sale;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SaleModelAssembler {

    private ModelMapper modelMapper;

    public SaleModel toModel(Sale sale) {
        return modelMapper.map(sale, SaleModel.class);
    }

    public List<SaleModel> toCollectionModel(List<Sale> sales) {
        return sales.stream().map(
                sale -> toModel(sale)).toList();
    }

}
