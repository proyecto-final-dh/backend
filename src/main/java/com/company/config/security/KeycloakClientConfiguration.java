package com.company.config.security;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.ClientBuilder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;


@Component
public class KeycloakClientConfiguration {
    @Value("${dh.keycloak.serverUrl}")
    private String serverUrl;
    @Value("${dh.keycloak.realm}")
    private String realm;
    @Value("${dh.keycloak.username}")
    private String username;
    @Value("${dh.keycloak.password}")
    private String password;

    @Value("${dh.keycloak.clientId}")
    private String clientId;

    @Value("${dh.keycloak.realmMaster}")
    private String realmMaster;



    @Bean
    public Keycloak getInstance() throws NoSuchAlgorithmException, KeyManagementException {

        ResteasyClientBuilder builder = new ResteasyClientBuilderImpl().disableTrustManager();


        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realmMaster)
                .username(username)
                .password(password)
                .clientId(clientId)
                .resteasyClient(builder.build())
                .build();
    }
}
