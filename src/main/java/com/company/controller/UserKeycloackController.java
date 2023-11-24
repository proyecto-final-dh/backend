package com.company.controller;


import com.company.model.entity.UserKeycloak;
import com.company.repository.UserKeycloakRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserKeycloackController {

    private UserKeycloakRepository userKeycloakRepository;

    public UserKeycloackController(UserKeycloakRepository userKeycloakRepository) {
        this.userKeycloakRepository = userKeycloakRepository;
    }

    @GetMapping
    public ResponseEntity<UserKeycloak> buscarid(){
        return  ResponseEntity.ok().body(userKeycloakRepository.findUser("b19dc58a-0836-425d-ae3e-814a816a523e"));

    }
}
