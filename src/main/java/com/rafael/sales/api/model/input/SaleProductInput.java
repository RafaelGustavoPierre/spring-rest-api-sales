package com.rafael.sales.api.model.input;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rafael.sales.api.model.ProductModel;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
public class SaleProductInput {

    private Long id;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigInteger quantity;

    @JoinColumn(name = "id_sale")
    @ManyToOne
    @JsonIgnore
    private SaleInput sale;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_product")
    private ProductModel product;

}
