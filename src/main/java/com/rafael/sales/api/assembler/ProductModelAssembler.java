package com.rafael.sales.api.assembler;

import com.rafael.sales.api.model.ProductModel;
import com.rafael.sales.domain.model.Product;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ProductModelAssembler {

    private ModelMapper modelMapper;

    public ProductModel toModel(Product product) {
        return modelMapper.map(product, ProductModel.class);
    }

    public List<ProductModel> toCollectionModel(List<Product> products) {
        return products.stream().map(
                product -> toModel(product))
                .toList();
    }

}
