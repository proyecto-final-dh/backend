package com.company.repository;

import com.company.model.entity.Pets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPetsRepository extends JpaRepository<Pets, Integer> {

    Pets findByName(String name);

}
