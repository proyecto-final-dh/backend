package com.company.service.interfaces;

import com.company.model.entity.Species;

import java.util.List;

public interface ISpeciesService {
    List<Species> getAllSpecies() throws Exception;
    Species getSpeciesById(int id);
    Species createSpecies(Species species);
    Species updateSpecies(int id, Species species);
    void deleteSpecies(int id);
}
