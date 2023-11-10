package com.company.controller;
import com.company.enums.PetStatus;
import com.company.model.entity.Pets;
import com.company.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }


    @GetMapping
    public Page<Pets> getAllPets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Pets> petPage = petService.findAll(pageable);
            return petPage;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/status")
    public Page<Pets> getPetsByStatus(
            @RequestParam PetStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Pets> petPage = petService .findByStatus(status,pageable);
            return petPage;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPetById(@PathVariable int id) {
        try {
            Pets pets = petService.findById(id);
            return ResponseEntity.ok(pets);
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the pet");
        }
    }

    @GetMapping("/locations/{id}")
    public List<Pets> getByLocation(@PathVariable int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        try {
            Pageable pageable = PageRequest.of(page,size);
            Page<Pets> petPage = petService.findByLocation(id,pageable);
            return petPage.getContent();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/owner/{id}")
    public List<Pets> getByOwner(@PathVariable int id,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "9") int size) {
        try {
            Pageable pageable = PageRequest.of(page,size);
            Page<Pets> petPage = petService.findByOwner(id,pageable);
            return petPage.getContent();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> createPet(@RequestBody Pets pets) {
        try {
            Pets newPets = petService.save(pets);
            return new ResponseEntity<>(newPets, HttpStatus.CREATED);
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePet(@PathVariable int id, @RequestBody Pets pets) {
        try {
            Pets updatedPets = petService.update(id, pets);
            return ResponseEntity.ok(updatedPets);
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the pet");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePet(@PathVariable int id) {
        try {
            petService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the pet");
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> filterPets(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String species,
            @RequestParam(required = false, name = "breed_id") Integer breedId,
            @RequestParam(required = false, name = "pet_size") String petSize,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) throws Exception {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Pets> filteredPets = petService.filterPets(location, species, breedId, petSize, status, pageable);
            return ResponseEntity.ok(filteredPets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }




}
