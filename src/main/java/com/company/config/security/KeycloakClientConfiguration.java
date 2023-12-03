package com.company.config.security;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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
