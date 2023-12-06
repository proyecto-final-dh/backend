package com.company.controller;

import com.company.enums.PetGender;
import com.company.enums.PetSize;
import com.company.exceptions.ResourceNotFoundException;
import com.company.model.dto.CompletePetDto;
import com.company.model.dto.CreatePetDto;
import com.company.enums.PetStatus;
import com.company.model.dto.PetWithUserInformationDto;
import com.company.model.dto.UpdatePetDto;
import com.company.model.entity.Pets;
import com.company.service.PetService;
import com.company.service.UserDetailsService;
import com.company.utils.ResponsesBuilder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

import java.util.List;

import static com.company.constants.Constants.PET_CREATED;
import static com.company.constants.Constants.PET_UPDATED;
import static com.company.constants.Constants.PET_GET_SUCCESS;

@AllArgsConstructor
@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private final ResponsesBuilder responsesBuilder;
    private final UserDetailsService userDetailsService;


    @GetMapping
    public Page<CompletePetDto> getAllPets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CompletePetDto> petPage = petService.findAll(pageable);
            return petPage;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/status")
    public Page<CompletePetDto> getPetsByStatus(
            @RequestParam PetStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CompletePetDto> petPage = petService .findByStatus(status,pageable);
            return petPage;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getPetById(@PathVariable int id) {
        try {
            PetWithUserInformationDto pets = petService.findById(id);
            return responsesBuilder.buildResponse(HttpStatus.OK.value(), PET_GET_SUCCESS, pets, null);

        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the pet");
        }
    }

    @GetMapping("/locations/{id}")
    public Page<CompletePetDto> getByLocation(@PathVariable int id,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "9") int size) {
        try {
            Pageable pageable = PageRequest.of(page,size);
            Page<CompletePetDto> petPage = petService.findByLocation(id,pageable);
            return petPage;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/owner/{id}")
    public Page<CompletePetDto> getByOwner(@PathVariable int id,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "9") int size) {
        try {
            Pageable pageable = PageRequest.of(page,size);
            Page<CompletePetDto> petPage = petService.findByOwner(id,pageable);
            return petPage;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @GetMapping("/gender")
    public Page<CompletePetDto> getPetsByGender(
            @RequestParam String gender,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CompletePetDto> petPage = petService .findByGender(gender,pageable);
            return petPage;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/size")
    public Page<CompletePetDto> getPetsBySize(
            @RequestParam String petSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CompletePetDto> petPage = petService .findBySize(petSize,pageable);
            return petPage;
        } catch (Exception e) {
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
    public ResponseEntity updatePet(@PathVariable int id,
                                    @RequestPart("post") UpdatePetDto pet,
                                    @RequestPart(value = "newImage", required = false) MultipartFile[] newImages) {
        return responsesBuilder.buildResponse(HttpStatus.OK.value(), PET_UPDATED, petService.update(id, pet, newImages), null);

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

    @GetMapping("/recommendation/{id}")
    public List<CompletePetDto> getPetsRecommendation(@PathVariable int id,@RequestParam(name = "limit", required = false) int limit)
    {
        try {
            return petService.findPetsRecommendations(id,limit);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<CompletePetDto>> filterPets(
            @RequestParam(required = false) Integer location,
            @RequestParam(required = false) Integer species,
            @RequestParam(required = false, name = "breed_id") Integer breedId,
            @RequestParam(required = false, name = "pet_size") String petSize,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CompletePetDto> filteredPets = petService.filterPets(location, species, breedId, petSize, status, pageable);
            return ResponseEntity.ok(filteredPets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/rescued-pet")
    public ResponseEntity<?>findbyOwnerByOwnerAndStatusAdopted() throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId=null;

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) authentication;
            userId = (String) jwtAuthToken.getTokenAttributes().get("sub");
        }

        Integer userDetailsId= userDetailsService.findByUserId(userId).getId();

        return ResponseEntity.status(HttpStatus.OK).body(petService.findbyOwnerByOwnerAndStatus(PetStatus.ADOPTADA,userDetailsId));
    }

    @GetMapping("/pet-for-adoption")
    public ResponseEntity<?>findbyOwnerByOwnerAndStatusAdoption() throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId=null;

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) authentication;
            userId = (String) jwtAuthToken.getTokenAttributes().get("sub");
        }

        Integer userDetailsId= userDetailsService.findByUserId(userId).getId();
        return ResponseEntity.status(HttpStatus.OK).body(petService.findbyOwnerByOwnerAndStatus(PetStatus.EN_ADOPCION,userDetailsId));
    }

}
