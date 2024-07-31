package com.rafael.sales.api.model.input;

import com.rafael.sales.domain.model.StatusSale;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SaleInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusSale status;

    @Column(name = "date_register")
    private OffsetDateTime dateRegister;

    @Valid
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SaleProductInput> items;

    @Valid
    @OneToOne
    @JoinColumn(name = "id_user")
    private SaleUser user;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.items.forEach(item -> {
            item.setSale(this);
        });
    }

}
