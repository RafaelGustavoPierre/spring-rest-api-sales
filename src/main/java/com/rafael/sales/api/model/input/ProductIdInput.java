package com.rafael.sales.api.model.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductIdInput {

    @NotBlank
    private Long id;

}
