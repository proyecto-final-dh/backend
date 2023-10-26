package com.company.controller;

import com.company.model.entity.Breeds;
import com.company.service.BreedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/breeds")
public class BreedsController {
    private final BreedsService breedsService;

    @Autowired
    public BreedsController(BreedsService breedsService) {
        this.breedsService = breedsService;
    }

    @GetMapping("/")
    public List<Breeds> getAllSpecies() {
        try {
            return breedsService.getAllBreeds();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSpeciesById(@PathVariable int id) {
        try {
            Breeds breeds = breedsService.getBreedsById(id);
            return ResponseEntity.ok(breeds);
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching breeds");
        }

    }
}
