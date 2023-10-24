package com.company.service;
import com.company.model.entity.Species;
import com.company.repository.ISpeciesRepository;
import org.hibernate.FetchNotFoundException;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class SpeciesService implements ISpeciesService{
    private final ISpeciesRepository speciesRepository;

    @Autowired
    public SpeciesService(ISpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    public List<Species> getAllSpecies() {
        return speciesRepository.findAll();
    }

    public Species getSpeciesById(int id) throws Exception {
        Optional<Species> species = speciesRepository.findById(id);
        if (species.isPresent()) {
            return species.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Species not found");
        }
    }

    public Species createSpecies(Species species) {
        if (species.getName() == null)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Name is required");
        if(speciesRepository.findByName(species.getName()).isPresent())
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Species already created");
        return speciesRepository.save(species);
    }

    public Species updateSpecies(int id, Species updatedSpecies) {
        Optional<Species> existingSpecies = speciesRepository.findById(id);

        if (existingSpecies.isPresent()) {
            Species species = existingSpecies.get();
            species.setName(updatedSpecies.getName());
            return speciesRepository.save(species);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Species does not exist");
        }
    }

    public void deleteSpecies(int id) {
        Optional<Species> species = speciesRepository.findById(id);
        if (species.isPresent()) {
            speciesRepository.deleteById(id);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Species not found");
        }
    }
}

