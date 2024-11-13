package com.rafael.sales.core.security.authorizationserver;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

public class CustomPasswordAuthenticationProvider implements AuthenticationProvider {

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    public CustomPasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService,
                                                OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                                UserDetailsService userDetailsService) {
        this.tokenGenerator = tokenGenerator;
        System.out.println("Iniciando -> CustomPasswordAuthenticationProvider");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println(tokenGenerator);

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
