package com.company.repository;

import com.company.model.entity.Breeds;
import com.company.model.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBreedsRepository extends JpaRepository<Breeds, Integer> {
    Optional<Breeds> findByName(String name);
    List<Breeds> findBySpecies(Species species);
}
