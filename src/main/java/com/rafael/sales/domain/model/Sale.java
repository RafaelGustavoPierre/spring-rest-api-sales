package com.rafael.sales.domain.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusSale status;

    @Column(name = "date_register")
    private OffsetDateTime dateRegister;

//    @Valid
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSale> items;

    @PrePersist
    public void prePersist() {
        this.items.forEach(item -> {
            item.setSale(this);
        });
    }

}
