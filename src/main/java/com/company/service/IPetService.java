package com.company.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.company.model.entity.Pet;

import java.util.List;

public interface IPetService {
    Page<Pet> findAll(Pageable pageable) throws Exception;
    Pet findById(Long id) throws Exception;
    Pet update(Long id, Pet pet) throws Exception;
    Pet save(Pet pet) throws Exception;
    void deleteById(Long id) throws Exception;
}