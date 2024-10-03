package com.rafael.sales.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Getter
@Setter
public class SaleItemModel {

    private Long id;
    private BigDecimal price;
    private BigDecimal quantity;

    private SaleProductModel product;
}
