package com.company.controller;

import com.company.model.entity.Species;
import com.company.service.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/species")
public class SpeciesController {
    private final SpeciesService speciesService;

    @Autowired
    public SpeciesController(SpeciesService speciesService) {
        this.speciesService = speciesService;
    }

    @GetMapping("/")
    public List<Species> getAllSpecies() {
        try {
            return speciesService.getAllSpecies();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSpeciesById(@PathVariable int id) {
        try {
            Species species = speciesService.getSpeciesById(id);
            return ResponseEntity.ok(species);
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching species");
        }

    }

    @PostMapping("/")
    public ResponseEntity<Object> createSpecies(@RequestBody Species species) {
        try {
            Species newSpecies = speciesService.createSpecies(species);
            return new ResponseEntity<>(newSpecies, HttpStatus.CREATED);
        }catch(ResponseStatusException ex){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Species updateSpecies(@PathVariable int id, @RequestBody Species updatedSpecies) {
        return speciesService.updateSpecies(id, updatedSpecies);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSpecies(@PathVariable int id) {
        try{
            speciesService.deleteSpecies(id);
            return ResponseEntity.noContent().build();
        }catch(ResponseStatusException ex){
            throw  new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting species");
        }
    }
}
