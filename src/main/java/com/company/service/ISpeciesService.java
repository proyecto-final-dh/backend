package com.company.service;

import com.company.model.entity.Species;

import java.util.List;
import java.util.Optional;

public interface ISpeciesService {
    List<Species> getAllSpecies() throws Exception;
    Species getSpeciesById(int id) throws Exception;
    Species createSpecies(Species species);
    //Species updateSpecies(int id, Species species);
    void deleteSpecies(int id);
}
