package com.rafael.sales.api.model.input;

import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleUser {

    @OneToOne
    @NotNull
    private Long id;

}
