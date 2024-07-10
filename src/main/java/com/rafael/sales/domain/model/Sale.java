package com.rafael.sales.domain.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String code;

    @NotBlank
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusSale status;

    @Column(name = "date_register")
    private OffsetDateTime dateRegister;

    @Valid
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductSale> items;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.items.forEach(item -> {
            item.setSale(this);
        });

        if (this.code == null) {
            setCode(UUID.randomUUID().toString());
        }
    }

}
