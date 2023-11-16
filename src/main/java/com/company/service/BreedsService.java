package com.company.service;

import com.company.model.dto.BreedsDTO;
import com.company.model.entity.Breeds;
import com.company.model.entity.Species;
import com.company.repository.IBreedsRepository;
import com.company.service.interfaces.IBreedsService;
import com.company.service.interfaces.ISpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BreedsService implements IBreedsService {
    private final IBreedsRepository breedsRepository;
    private final ISpeciesService speciesService;

    @Autowired
    public BreedsService(IBreedsRepository breedsRepository, ISpeciesService speciesService) {
        this.breedsRepository = breedsRepository;
        this.speciesService = speciesService;
    }

    @Override
    public List<Breeds> getAllBreeds() {
        return breedsRepository.findAll();
    }

    @Override
    public Breeds getBreedsById(int id) {
        Optional<Breeds> breeds = breedsRepository.findById(id);
        if (breeds.isPresent()) {
            return breeds.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Breed not found");
        }
    }

    @Override
    public Breeds createBreeds(Breeds breeds) {
        validatePayload(breeds);
        validateNameBreed(0, breeds, "POST");

        Species species = speciesService.getSpeciesById(breeds.getSpecies().getId());
        breeds.setSpecies(species);
        return breedsRepository.save(breeds);
    }

    @Override
    public Breeds updateBreeds(int id, Breeds updatedBreeds) {
        Optional<Breeds> existingBreeds = breedsRepository.findById(id);
        if (existingBreeds.isPresent()) {
            validatePayload(updatedBreeds);
            validateNameBreed(id, updatedBreeds, "PUT");
            Species species = speciesService.getSpeciesById(updatedBreeds.getSpecies().getId());
            Breeds breedsDB = existingBreeds.get();
            breedsDB.setName(updatedBreeds.getName());
            breedsDB.setSpecies(species);
            return breedsRepository.save(breedsDB);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Breed does not exist");
        }
    }

    private void validatePayload(Breeds breeds) {
        if (breeds.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        int speciesID = breeds.getSpecies().getId();
        if (speciesID == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Species id is required");
        }
        Species species = speciesService.getSpeciesById(speciesID);
        if (species == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Species does not exist");
        }
    }

    private void validateNameBreed(int id, Breeds breeds, String req) {
        boolean conflict = false;
        if ("POST".equals(req)) {
            //POST: Valida que el nombre no se encuentre en la BD
            if (breedsRepository.findByName(breeds.getName()).isPresent()) {
                conflict = true;
            }
        } else {
            //PUT: Valida que el nombre no se encuentre en la BD bajo otro id de registro
            Optional<Breeds> existingBreedsWithName = breedsRepository.findByName(breeds.getName());
            if (existingBreedsWithName.isPresent()) {
                Breeds existingBreeds = existingBreedsWithName.get();
                if (existingBreeds.getId() != id) {
                    conflict = true;
                }
            }
        }
        if (conflict) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Breed already created");
        }
    }

    @Override
    public void deleteBreeds(int id) {
        Optional<Breeds> breeds = breedsRepository.findById(id);
        if (breeds.isPresent()) {
            breedsRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Breed not found");
        }
    }

    @Override
    public List<BreedsDTO> getBreedsBySpecies(int speciesId) {
        Species species = speciesService.getSpeciesById(speciesId);
        List<Breeds> breedsList = breedsRepository.findBySpecies(species) ;
        List<BreedsDTO> breedsDtoList = breedsList.stream()
                .map(breed -> new BreedsDTO(breed.getId(), breed.getName()))
                .collect(Collectors.toList());
        return breedsDtoList;
    }
}

