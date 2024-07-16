package com.rafael.sales.api.model.input;

import com.rafael.sales.domain.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductMediaInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_product")
    @OneToOne
    private Product product;

    @NotBlank
    private String fileName;

    @NotBlank
    private String contentType;

    @NotNull
    private Long size;


}
