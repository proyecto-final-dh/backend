package com.company.controller;


import com.company.model.entity.UserKeycloak;
import com.company.repository.UserKeycloakRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserKeycloackController {

    private UserKeycloakRepository userKeycloakRepository;

    public UserKeycloackController(UserKeycloakRepository userKeycloakRepository) {
        this.userKeycloakRepository = userKeycloakRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserKeycloak> buscarid(@PathVariable String id){
        return  ResponseEntity.ok().body(userKeycloakRepository.findUser(id));

    }
}
