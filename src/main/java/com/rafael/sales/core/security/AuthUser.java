package com.rafael.sales.core.security;

import com.rafael.sales.domain.model.Seller;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AuthUser extends User {

    private String fullName;

    public AuthUser(Seller seller, Collection<? extends GrantedAuthority> authorities) {
        super(seller.getEmail(), seller.getPassword(), authorities);

        this.fullName = seller.getName();
    }
}
