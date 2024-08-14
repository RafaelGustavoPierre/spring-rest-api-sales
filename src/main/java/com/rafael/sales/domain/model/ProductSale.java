package com.rafael.sales.domain.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class ProductSale {

    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private BigDecimal price;

    private BigInteger quantity;

    @JoinColumn(name = "id_sale")
    @ManyToOne
    private Sale sale;

    @JoinColumn(name = "id_product")
    @ManyToOne
    private Product product;

}
