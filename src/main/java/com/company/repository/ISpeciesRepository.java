package com.company.repository;

import com.company.model.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISpeciesRepository extends JpaRepository<Species, Integer> {
    Optional<Species> findByName1(String name);
}