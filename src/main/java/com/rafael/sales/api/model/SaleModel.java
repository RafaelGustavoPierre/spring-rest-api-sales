package com.rafael.sales.api.model;

import com.rafael.sales.domain.model.StatusSale;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class SaleModel {

    private Long id;
    private String description;
    private StatusSale status;
    private OffsetDateTime dateRegister;

    private List<SaleItemModel> items;

}
