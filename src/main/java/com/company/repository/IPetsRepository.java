package com.company.repository;

import com.company.model.entity.Pets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IPetsRepository extends JpaRepository<Pets, Integer> {

    Pets findByName1(String name1);


    @Query(value = "SELECT pets.* " +
            "FROM petPI.pets AS pets " +
            "WHERE pets.user_details_id IN " +
            "(SELECT ud.id FROM petPI.user_details AS ud WHERE ud.location_id = :id )", nativeQuery = true)
    Page<Pets> findByLocation(@Param("id") int id, Pageable pageable);

    @Query(value = "SELECT pets.* " +
            "FROM petPI.pets AS pets " +
            "WHERE pets.user_details_id = :id ", nativeQuery = true)
    Page<Pets> findByOwner(@Param("id") int id, Pageable pageable);


}
