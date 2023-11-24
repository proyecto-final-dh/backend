package com.company.repository;

import com.company.model.entity.UserKeycloak;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserKeycloakRepository {
    private final Keycloak keycloakClient;

    @Value("${dh.keycloak.realm}")
    private String realm;

    public UserKeycloakRepository(Keycloak keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    public UserKeycloak findUser(String keycloakUserId){
        UserRepresentation usersRepresentation=keycloakClient.realm(realm).users().get(keycloakUserId).toRepresentation();
        UserKeycloak userResponse = new UserKeycloak();
        userResponse.setUserName(usersRepresentation.getUsername());
        userResponse.setEmail(usersRepresentation.getEmail());
        userResponse.setName(usersRepresentation.getFirstName());
        userResponse.setLastname(usersRepresentation.getLastName());
        userResponse.setIdUsuario(keycloakUserId);

        return userResponse;
    }

}
