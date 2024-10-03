package com.rafael.sales.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class SaleProductModel extends RepresentationModel<SaleProductModel> {

    private Long id;
    private String name;

}
