package com.company.service;

import com.company.model.entity.Breeds;
import com.company.repository.IBreedsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class BreedsService implements IBreedsService{
    private final IBreedsRepository breedsRepository;
    @Autowired
    public BreedsService(IBreedsRepository breedsRepository) {
        this.breedsRepository = breedsRepository;
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
}
