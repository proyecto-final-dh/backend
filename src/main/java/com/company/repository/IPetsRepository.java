package com.company.repository;

import com.company.enums.PetStatus;
import com.company.model.entity.Pets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface IPetsRepository extends JpaRepository<Pets, Integer> , JpaSpecificationExecutor<Pets> {

    Pets findByName(String name);
    Page<Pets> findByStatus(PetStatus status, Pageable pageable);


/*
    @Query("SELECT p FROM Pets p WHERE p.status = (SELECT status FROM Pets WHERE id = :petId)")
    List<Pets> findPetsRecommendations(@Param("petId") int petId);


    @Query("SELECT p FROM Pets p " +
            "WHERE p.status = (SELECT status FROM Pets WHERE id = :petId) " +
            "AND p.breed.species.id = (SELECT b.species.id FROM Breeds b WHERE b.id = (SELECT breed.id FROM Pets WHERE id = :petId))")
    List<Pets> findPetsRecommendations(@Param("petId") int petId);


    @Query("SELECT p FROM Pets p " +
            "WHERE p.status = (SELECT status FROM Pets WHERE id = :petId) " +
            "AND p.breed.species.id = (SELECT b.species.id FROM Breeds b WHERE b.id = (SELECT breed.id FROM Pets WHERE id = :petId)) " +
            "AND p.gender = (SELECT gender FROM Pets WHERE id = :petId) " +
            "AND p.size = (SELECT size FROM Pets WHERE id = :petId)")
    List<Pets> findPetsRecommendations(@Param("petId") int petId);

*/


    @Query("SELECT p FROM Pets p " +
            "WHERE p.status = (SELECT status FROM Pets WHERE id = :petId) " +
            "AND p.breed.species.id = (SELECT b.species.id FROM Breeds b WHERE b.id = (SELECT breed.id FROM Pets WHERE id = :petId)) " +
            "AND p.gender = (SELECT gender FROM Pets WHERE id = :petId) " +
            "AND p.size = (SELECT size FROM Pets WHERE id = :petId)")
    List<Pets> findPetsRecommendationsAll(@Param("petId") int petId);




    @Query("SELECT p FROM Pets p " +
            "WHERE p.status = (SELECT status FROM Pets WHERE id = :petId) " +
            "AND p.breed.species.id = (SELECT b.species.id FROM Breeds b WHERE b.id = (SELECT breed.id FROM Pets WHERE id = :petId)) ")
    List<Pets> findPetsRecommendationsSpecieStatus(@Param("petId") int petId);


    @Query("SELECT p FROM Pets p " +
            "WHERE p.status = (SELECT status FROM Pets WHERE id = :petId) ")
    List<Pets> findPetsRecommendationsStatus(@Param("petId") int petId);



}
