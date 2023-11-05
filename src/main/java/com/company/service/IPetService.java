package com.company.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.company.model.entity.Pets;

public interface IPetService {
    Page<Pets> findAll(Pageable pageable) throws Exception;
    Pets findById(int id) throws Exception;
    Pets update(int id, Pets pets) throws Exception;
    Pets save(Pets pets) throws Exception;
    void deleteById(int id) throws Exception;
    Page<Pets> findByLocation(int id, Pageable pageable) throws Exception;
    Page<Pets> findByOwner(int id, Pageable pageable) throws Exception;
}