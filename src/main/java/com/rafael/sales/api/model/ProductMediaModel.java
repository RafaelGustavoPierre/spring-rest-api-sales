package com.rafael.sales.api.model;

import com.rafael.sales.domain.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductMediaModel {

    private String fileName;

}
