package com.company.repository;

import com.company.enums.PetStatus;
import com.company.model.entity.Pets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IPetsRepository extends JpaRepository<Pets, Integer> , JpaSpecificationExecutor<Pets> {

    Pets findByName(String name);
    Page<Pets> findByStatus(PetStatus status, Pageable pageable);

}
