package com.rafael.sales.core.security.authorizationserver;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.rafael.sales.core.property.AppProperties;

import com.rafael.sales.domain.exception.EntityNotFoundException;
import com.rafael.sales.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyStore;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AppProperties properties;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(this.properties.getSecurity().getProviderUrl())
                .build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient testeCode = RegisteredClient
                .withId("2")
                .clientId("testecode")
                .clientSecret(passwordEncoder.encode("123"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes(types -> types.addAll(Arrays.asList(AuthorizationGrantType.REFRESH_TOKEN, AuthorizationGrantType.AUTHORIZATION_CODE)))
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .scope("READ")
                .scope("WRITE")
                .scope("CAN_READ_REQUESTS")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofDays(1))
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();

        RegisteredClient segundoUser = RegisteredClient
                .withId("2")
                .clientId("segundouser")
                .clientSecret(passwordEncoder.encode("123"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes(types -> types.addAll(Arrays.asList(AuthorizationGrantType.REFRESH_TOKEN, AuthorizationGrantType.AUTHORIZATION_CODE)))
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .scope("READ")
                .scope("WRITE")
                .scope("CAN_READ_REQUESTS")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofDays(1))
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(segundoUser);
    }

    @Bean
    public JWKSource<SecurityContext> jwtSource() throws Exception {
        final var keyStorePass = this.properties.getSecurity().getPassword().toCharArray();
        final var keypairAlias = this.properties.getSecurity().getKeypairAlias();
        final var jksResource = this.properties.getSecurity().getJksResource();

        final var inputStream = jksResource.getInputStream();
        final var keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, keyStorePass);

        final var rsaKey = RSAKey.load(keyStore, keypairAlias, keyStorePass);

        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            final var authentication = context.getPrincipal();
            if (authentication.getPrincipal() instanceof User user) {
                final var userByEmail = this.userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new EntityNotFoundException("user-3"));

                final var authorities = new HashSet<String>();

                for (GrantedAuthority authority : authentication.getAuthorities()) authorities.add(authority.getAuthority());

                context.getClaims().claim("userId", userByEmail.getId().toString());
                context.getClaims().claim("username", userByEmail.getName());
                context.getClaims().claim("email", userByEmail.getEmail());
                context.getClaims().claim("authorities", authorities);
            }
        };
    }

}
