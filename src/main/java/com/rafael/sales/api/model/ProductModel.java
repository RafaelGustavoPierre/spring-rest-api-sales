package com.rafael.sales.api.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class ProductModel {

    private Long id;
    private String name;
    private BigInteger quantity;

}
