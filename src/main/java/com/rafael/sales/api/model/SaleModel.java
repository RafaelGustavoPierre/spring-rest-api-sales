package com.rafael.sales.api.model;

import com.rafael.sales.domain.model.StatusSale;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.OffsetDateTime;
import java.util.List;

@Relation(collectionRelation = "sales")
@Getter
@Setter
public class SaleModel extends RepresentationModel<SaleModel> {

    private SaleSellerModel seller;
    private SaleClientModel client;
    private String code;
    private String description;
    private StatusSale status;
    private OffsetDateTime dateRegister;

    private List<SaleItemModel> items;

}
