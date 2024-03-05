package com.rafael.sales.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigInteger quantity;

    @JoinColumn(name = "id_sale")
    @ManyToOne
    @JsonIgnore
    private Sale sale;

    @JoinColumn(name = "id_product")
    @NotNull
    @ManyToOne
    private Product product;

}
