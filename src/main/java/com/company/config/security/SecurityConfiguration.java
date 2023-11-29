package com.company.config.security;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import javax.net.ssl.SSLContext;


import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = false, securedEnabled = false, jsr250Enabled = true)
public class SecurityConfiguration {


    @Value("${security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;


    @Bean
    public JwtDecoder jwtDecoder() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RestTemplate restTemplate = restTemplate();
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).restOperations(restTemplate).build();
        // Configura el resto del JwtDecoder como lo necesites
        return jwtDecoder;
    }


    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        final TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;

        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(csf).build();

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(jwt -> {
            try {
                jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(new JwtAuthenticationConverter());
            } catch (KeyStoreException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (KeyManagementException e) {
                throw new RuntimeException(e);
            }
        }));

        http.cors(cors -> cors.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST,"/locations/**","/species/**","/breeds/**","/pets/**").authenticated()
                        .requestMatchers("/user-details/**","/history/**").authenticated()
                        .anyRequest().permitAll());
        return http.build();
    }


}
