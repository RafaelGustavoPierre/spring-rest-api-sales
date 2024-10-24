package com.rafael.sales.core.security;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomAuthoritiesJwtToken implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);

        return authorities.stream()
                .map(grantedAuthority -> {
                    String authority = grantedAuthority.getAuthority();

                    if (!authority.equals("SCOPE_READ") && !authority.equals("SCOPE_WRITE")) {
                        authority = authority.substring(6);
                    }
                    String finalAuthority = authority;
                    return (GrantedAuthority) () -> finalAuthority;
                })
                .collect(Collectors.toList());

    }

}
