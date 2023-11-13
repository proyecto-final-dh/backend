package com.company.service;

import com.company.model.entity.Pets;
import com.company.enums.PetStatus;
import com.company.model.entity.Location;
import com.company.model.entity.UserDetails;
import com.company.repository.IPetsRepository;
import com.company.repository.IUserDetailsRepository;
import com.company.repository.LocationRepository;
import jakarta.persistence.criteria.Join;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.company.constants.Constants.LOCATION_NOT_FOUND;
import static com.company.constants.Constants.OWNER_NOT_FOUND;

@AllArgsConstructor
@Service
public class PetService implements  IPetService{

    private IPetsRepository IPetsRepository;
    private IUserDetailsRepository userDetailsRepository;
    private final LocationRepository locationRepository;

    public Page<Pets> findAll(Pageable pageable) throws Exception {
        try {
            return IPetsRepository.findAll(pageable);
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas paginadas.");
        }
    }

    public Pets findById(int id) throws Exception {
        try {
            Optional<Pets> pet = IPetsRepository.findById(id);
            if (pet.isPresent()) {
                return pet.get();
            } else {
                throw new Exception("Pet with id " + id + " not found.");
            }
        } catch (Exception e) {
            throw new Exception("Pet with id " + id + " not found.");
        }
    }


    public Pets update(int id, Pets updatedPets) {
        Optional<Pets> existingPet = IPetsRepository.findById(id);

        if (existingPet.isPresent()) {
            Pets pets = existingPet.get();
            pets.setName(updatedPets.getName());
            pets.setStatus(updatedPets.getStatus());
            pets.setGender(updatedPets.getGender());
            pets.setSize(updatedPets.getSize());
            pets.setDescription(updatedPets.getDescription());

            return IPetsRepository.save(pets);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Pet with ID " + id + " does not exist");
        }
    }

    public Pets save(Pets pets) throws Exception {
        if (!pets.getName().isEmpty()) {
            return IPetsRepository.save(pets);
        } else {
            throw new Exception("Pet Name not found");
        }
    }

    public void deleteById(int id) throws Exception {
        Optional<Pets> species = IPetsRepository.findById(id);
        if (species.isPresent()) {
            IPetsRepository.deleteById( id);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }
    }

    @Override
    public Page<Pets> findByStatus(PetStatus status, Pageable pageable) throws Exception {
        try {
            return IPetsRepository.findByStatus(status, pageable);
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas por status.");
        }
    }

    @Override
    public Page<Pets> filterPets(String location, String species, Integer breedId, String size, String status, Pageable pageable) throws Exception {
        try {
            Specification<Pets> spec = buildSpecification(location, species, breedId, size, status);

            return IPetsRepository.findAll(spec, pageable);
        } catch (Exception e) {
            throw new Exception("Error al filtrar mascotas");
        }
    }


    private Specification<Pets> buildSpecification(String location, String species, Integer breedId, String size, String status) {
        Specification<Pets> spec = Specification.where(null);

        if (location != null && !location.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                Join<Pets, UserDetails> userDetailsJoin = root.join("userDetails");
                Join<UserDetails, Location> locationJoin = userDetailsJoin.join("location");
                String likeExpression = "%" + location + "%";
                return cb.like(locationJoin.get("city"), likeExpression);
            });
        }

        if (species != null && !species.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("breed").get("species").get("name"), species));
        }

        if (breedId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("breed").get("id"), breedId));
        }

        if (size != null && !size.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("size"), size));
        }


        if (status != null && !status.isEmpty()) {
            PetStatus petStatus = PetStatus.valueOf(status);
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), petStatus));
        }

        return spec;
    }

    public Page<Pets> findByLocation(int id, Pageable pageable) throws Exception {
        validateLocation(id);
        try {
            return IPetsRepository.findByLocation(id, pageable);
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas paginadas.");
        }
    }

    public Page<Pets> findByOwner(int id, Pageable pageable) throws Exception {
        validateUserDetails(id);
        try {
            return IPetsRepository.findByOwner(id, pageable);
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas paginadas.");
        }
    }

    private UserDetails validateUserDetails(int id) {
        Optional<UserDetails> userDetails = userDetailsRepository.findById(id);
        if (userDetails.isPresent()) {
            return userDetails.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,OWNER_NOT_FOUND);
        }
    }

    private Location validateLocation(int id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            return location.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,LOCATION_NOT_FOUND);
        }
    }

}
