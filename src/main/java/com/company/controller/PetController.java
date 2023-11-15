package com.company.controller;

import com.company.model.dto.CreatePetDto;
import com.company.enums.PetStatus;
import com.company.model.entity.Pets;
import com.company.service.PetService;
import com.company.utils.ResponsesBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static com.company.constants.Constants.PET_CREATED;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private final ResponsesBuilder responsesBuilder;

    @Autowired
    public PetController(PetService petService, ResponsesBuilder responsesBuilder) {
        this.petService = petService;
        this.responsesBuilder = responsesBuilder;
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

    @PostMapping("/own-with-images")
    public ResponseEntity createOwnPetWithImages(@RequestPart("post") CreatePetDto pet,
                                                 @RequestPart(value = "image", required = false) MultipartFile[] images) {
        return responsesBuilder.buildResponse(HttpStatus.CREATED.value(), PET_CREATED, petService.saveOwnPetWithImages(pet, images), null);
    }

    @PostMapping("/adoptive-with-images")
    public ResponseEntity createAdoptivePetWithImages(@RequestPart("post") CreatePetDto pet,
                                                      @RequestPart(value = "image") MultipartFile[] images) {
        return responsesBuilder.buildResponse(HttpStatus.CREATED.value(), PET_CREATED, petService.saveAdoptivePetWithImages(pet, images), null);
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
