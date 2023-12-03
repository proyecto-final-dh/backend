package com.company.controller;


import com.company.exceptions.ResourceNotFoundException;
import com.company.repository.UserKeycloakRepository;
import com.company.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserKeycloakRepository userKeycloakRepository;
    private UserService userService;

    public UserController(UserKeycloakRepository userKeycloakRepository, UserService userService) {
        this.userKeycloakRepository = userKeycloakRepository;
        this.userService = userService;
    }



    @GetMapping("/{userId}")
    public ResponseEntity<?> findUserById(@PathVariable String userId) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(userService.findById(userId));
    }

    @GetMapping
    public ResponseEntity<?> findUserById() throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";

        if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
            userId = jwtAuthToken.getToken().getClaims().get("sub").toString();
        }
        return ResponseEntity.ok().body(userService.findById(userId));
    }



}
