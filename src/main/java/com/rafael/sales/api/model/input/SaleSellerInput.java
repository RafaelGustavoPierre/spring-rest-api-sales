package com.rafael.sales.api.model.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleSellerInput {

    @NotNull
    private Long id;

}
