package com.rafael.sales.api.model;

import com.rafael.sales.domain.model.ProductMedia;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@Getter
@Setter
public class ProductModel {

    private Long id;
    private String name;
    private BigInteger quantity;

}
