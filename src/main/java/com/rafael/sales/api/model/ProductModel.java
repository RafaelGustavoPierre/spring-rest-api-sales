package com.rafael.sales.api.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class ProductModel {

    private String name;
    private BigInteger quantity;

}
