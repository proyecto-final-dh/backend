package com.company.config.security;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;


import javax.net.ssl.SSLContext;


import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                        .setSslContext(sslContext)
                        .setTlsVersions(TLS.V_1_3)
                        .build())
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager( connectionManager)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }






    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.
                oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(jwt ->
                                {
                                    try {
                                        jwt.decoder(jwtDecoder())
                                                .jwtAuthenticationConverter( new JwtAuthenticationConverter());
                                    } catch (KeyStoreException e) {
                                        throw new RuntimeException(e);
                                    } catch (NoSuchAlgorithmException e) {
                                        throw new RuntimeException(e);
                                    } catch (KeyManagementException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
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

/*

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


*/

}
