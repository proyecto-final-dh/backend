package com.company.repository;

import com.company.enums.PetStatus;
import com.company.model.entity.Pets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPetsRepository extends JpaRepository<Pets, Integer> {

    Pets findByName(String name);
    Page<Pets> findByStatus(PetStatus status, Pageable pageable);

}
