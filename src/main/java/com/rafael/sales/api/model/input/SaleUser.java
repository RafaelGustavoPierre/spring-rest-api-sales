package com.rafael.sales.api.model.input;

import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleUser {

    @OneToOne
    private Long id;

}
