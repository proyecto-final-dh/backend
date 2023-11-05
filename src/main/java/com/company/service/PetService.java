package com.company.service;

import com.company.model.entity.Pets;
import com.company.repository.IPetsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PetService implements  IPetService{

    private IPetsRepository IPetsRepository;

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
    public List<Pets> filterPets(String location, String species, String breed, String size) {
        Specification<Pets> spec = buildSpecification(location, species, breed, size);
        return IPetsRepository.findAll(spec);
    }

    private Specification<Pets> buildSpecification(String location, String species, String breed, String size) {
        Specification<Pets> spec = Specification.where(null);

        if (location != null && !location.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("locationID"), Long.parseLong(location)));
        }

        if (species != null && !species.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("species"), species));
        }

        if (breed != null && !breed.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("breedID"), Long.parseLong(breed)));
        }

        if (size != null && !size.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("size"), size));
        }

        return spec;
    }


}
