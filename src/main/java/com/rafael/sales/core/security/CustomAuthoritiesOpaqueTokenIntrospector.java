package com.rafael.sales.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomAuthoritiesOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OpaqueTokenIntrospector delegate;

    public CustomAuthoritiesOpaqueTokenIntrospector(OpaqueTokenIntrospector delegate) {
        this.delegate = delegate;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = delegate.introspect(token);
        Collection<GrantedAuthority> authorities = extractAuthorities(principal);

        return new OAuth2AuthenticatedPrincipal() {
            @Override
            public Collection<GrantedAuthority> getAuthorities() {
                return authorities;
            }

            @Override
            public Map<String, Object> getAttributes() {
                return principal.getAttributes();
            }

            @Override
            public String getName() {
                return principal.getName();
            }
        };
    }

    private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {
        List<String> scopes = principal.getAttribute("scope");
        System.out.println(scopes);
        return scopes.stream()
                .map(scope -> {
                    if ("READ".equals(scope) || "WRITE".equals(scope)) {
                        return new SimpleGrantedAuthority("SCOPE_" + scope);
                    } else {
                        return new SimpleGrantedAuthority(scope);
                    }
                })
                .collect(Collectors.toList());
    }

}
