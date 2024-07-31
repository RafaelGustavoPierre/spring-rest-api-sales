package com.rafael.sales.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class UserModel {

    private Long id;

    private String name;
    private String email;
    private String password;

    private OffsetDateTime dateRegister;

}
