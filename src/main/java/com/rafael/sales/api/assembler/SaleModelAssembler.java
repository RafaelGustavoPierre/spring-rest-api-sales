package com.rafael.sales.api.assembler;

import com.rafael.sales.api.resource.ProductResource;
import com.rafael.sales.api.resource.SaleResource;
import com.rafael.sales.api.model.SaleModel;
import com.rafael.sales.domain.model.Sale;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class SaleModelAssembler
        extends RepresentationModelAssemblerSupport<Sale, SaleModel> {

    @Autowired
    private ModelMapper modelMapper;

    public SaleModelAssembler() {
        super(SaleResource.class, SaleModel.class);
    }

    @Override
    public SaleModel toModel(Sale sale) {
        SaleModel saleModel = createModelWithId(sale.getCode(), sale);

        modelMapper.map(sale, saleModel);

        saleModel.getItems().forEach(saleItemModel -> {
            saleItemModel.getProduct().add(linkTo(ProductResource.class)
                    .slash(saleItemModel.getProduct().getId()).withSelfRel());
        });

        return saleModel;
    }

    @Override
    public CollectionModel<SaleModel> toCollectionModel(Iterable<? extends Sale> entities) {
        return super.toCollectionModel(entities).add(linkTo(SaleResource.class).withSelfRel());
    }
}
