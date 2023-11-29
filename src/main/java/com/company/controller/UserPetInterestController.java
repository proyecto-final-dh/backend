package com.company.controller;

import com.company.service.interfaces.IUserPetInterestService;
import com.company.utils.ResponsesBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interests")
public class UserPetInterestController {

    private final IUserPetInterestService userPetInterestService;
    private final ResponsesBuilder responseBuilder;

    public UserPetInterestController(IUserPetInterestService userPetInterestService, ResponsesBuilder responseBuilder) {
        this.userPetInterestService = userPetInterestService;
        this.responseBuilder = responseBuilder;
    }

    @GetMapping("/by-pet/{petId}")
    public ResponseEntity getUserPetInterest(@PathVariable("petId") int petId) {
        return responseBuilder.buildResponse(HttpStatus.OK.value(), "Get User interest status successfully",userPetInterestService.getUserPetInterest(petId), null);
    }

    @GetMapping
    public ResponseEntity getUserInterestsList() {
        return responseBuilder.buildResponse(HttpStatus.OK.value(), "Get User interests status successfully",userPetInterestService.getUserPetListInterests(), null);
    }

    @PostMapping("/{petId}")
    public ResponseEntity createUserPetInterest(@PathVariable("petId") int petId) {
        return responseBuilder.buildResponse(HttpStatus.OK.value(), "User interest created successfully",userPetInterestService.createUserPetInterest(petId), null);
    }
}
