package com.company.service.interfaces;

import com.company.model.entity.Breeds;
import com.company.model.entity.Species;

import java.util.List;

public interface IBreedsService {
    List<Breeds> getAllBreeds();
    Breeds getBreedsById(int id);
    Breeds createBreeds(Breeds breeds);
    Breeds updateBreeds(int id, Breeds breeds);
    void deleteBreeds(int id);
}
