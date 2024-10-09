package com.rafael.sales.core.security;

import com.rafael.sales.domain.exception.EntityNotFoundException;
import com.rafael.sales.domain.model.Seller;
import com.rafael.sales.domain.repository.SellerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private static final String USERNAME_NOT_FOUND = "Nome de Usuário não foi encontrado ou não existe";

    private final SellerRepository sellerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Seller seller = sellerRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(USERNAME_NOT_FOUND));

        return new AuthUser(seller, Collections.emptyList());
    }

}
