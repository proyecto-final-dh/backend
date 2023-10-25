package com.company.service;

import com.company.model.entity.Breeds;

import java.util.List;

public interface IBreedsService {
    List<Breeds> getAllBreeds();
    Breeds getBreedsById(int id);
    Breeds createBreeds(Breeds breeds);
}
