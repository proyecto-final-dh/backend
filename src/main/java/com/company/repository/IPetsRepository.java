package com.company.repository;

import com.company.model.entity.Pets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPetsRepository extends JpaRepository<Pets, Integer> {

    Pets findByName(String name);


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
