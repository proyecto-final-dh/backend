package com.company.controller;
import com.company.model.entity.Pet;
import com.company.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets") // Define la ruta base para todas las solicitudes relacionadas con Pet
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<Pet> getAllPets() {
        return petService.findAll();
    }

    @GetMapping("/{id}")
    public Pet getPetById(@PathVariable Long id) throws Exception {
        return petService.findById(id);
    }

    @PostMapping
    public Pet createPet(@RequestBody Pet pet) throws Exception {
        return petService.save(pet);
    }

    @PutMapping("/{id}")
    public Pet updatePet(@PathVariable Long id, @RequestBody Pet pet) throws Exception {
        return petService.update(id, pet);
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id) throws Exception {
        petService.deleteById(id);
    }
}