package com.company.controller;

import com.company.exceptions.BadRequestException;
import com.company.exceptions.ResourceNotFoundException;
import com.company.model.dto.SaveUserDetailsDto;
import com.company.model.entity.UserDetails;
import com.company.service.UserDetailsService;
import com.company.utils.ApiResponse;
import com.company.utils.ResponsesBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {
    @Autowired
    private UserDetailsService service;

    @Autowired
    private ResponsesBuilder responsesBuilder;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDetails>, Object>> findAll(){
        List<UserDetails> items = service.findAll();
        return responsesBuilder.buildResponse(HttpStatus.OK.value(),"Get User Details List successfully",items, null);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<UserDetails, Object>> findById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        UserDetails item = service.findById(id);
        return responsesBuilder.buildResponse(HttpStatus.OK.value(),"Get User Detail successfully", item,  null);
    }

    @GetMapping(path = "/user/{id}")
    public ResponseEntity<ApiResponse<UserDetails, Object>> findByUserId(@PathVariable("id") String id) throws ResourceNotFoundException {
        UserDetails item = service.findByUserId(id);
        return responsesBuilder.buildResponse(HttpStatus.OK.value(),"Get User Detail successfully", item,  null);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDetails, Object>> save(@Valid @RequestBody SaveUserDetailsDto item) throws ResourceNotFoundException, BadRequestException {
        UserDetails response = service.save(item);
        return responsesBuilder.buildResponse(HttpStatus.CREATED.value(),"User Details created successfully", response, null);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<UserDetails, Object>> update(@PathVariable("id") Long id, @Valid @RequestBody SaveUserDetailsDto user) throws ResourceNotFoundException, BadRequestException {
        UserDetails response = service.update(id, user);
        return responsesBuilder.buildResponse(HttpStatus.CREATED.value(),"User Details updated successfully", response, null);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable("id") Long id) throws ResourceNotFoundException, BadRequestException {
        service.deleteById(id);
        return responsesBuilder.buildResponse(HttpStatus.OK.value(),"User Details deleted successfully", null, null);
    }

}