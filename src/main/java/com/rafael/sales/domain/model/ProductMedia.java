package com.rafael.sales.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_product")
    @OneToOne
    private Product product;

    private String fileName;

    private String contentType;

    private Long size;

}
