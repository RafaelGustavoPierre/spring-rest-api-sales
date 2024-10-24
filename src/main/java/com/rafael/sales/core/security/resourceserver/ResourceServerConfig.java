package com.rafael.sales.core.security.resourceserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http, OpaqueTokenIntrospector introspector) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(
                        resourceServer -> resourceServer.jwt(
                                jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
//                        resourceServer -> resourceServer.opaqueToken(opaqueTokenConfigurer ->
//                                opaqueTokenConfigurer.introspector(new CustomAuthoritiesOpaqueTokenIntrospector(introspector)))
                )
                .headers(headers -> headers
                        .httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable
                        )
                );

        return http.build();
    }


    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        final var converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            final var authorities = jwt.getClaimAsStringList("authorities");

            if (authorities == null) return Collections.emptyList();

            final var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
            final var grantedAuthorities = authoritiesConverter.convert(jwt);

            grantedAuthorities.addAll(
                    authorities.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList());

            return grantedAuthorities;
        });
        return converter;
    }

}
