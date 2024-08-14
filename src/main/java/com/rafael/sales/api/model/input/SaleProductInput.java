package com.rafael.sales.api.model.input;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rafael.sales.api.model.ProductModel;
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

    @JsonIgnore
    private SaleInput sale;

    @NotNull
    private ProductModel product;

}
