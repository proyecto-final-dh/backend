package com.company.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {


    @Value("${security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

        @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.
                oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwt ->
                        jwt.decoder(jwtDecoder())
                                .jwtAuthenticationConverter( new JwtAuthenticationConverter())
                )
        );

            http
                    .cors(cors -> cors.disable())


                    .sessionManagement((session) -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                .authorizeRequests()
                .anyRequest().authenticated();
        return http.build();
    }





    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }




}
