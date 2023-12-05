package com.company.repository;

import com.company.enums.PetStatus;
import com.company.model.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IHistoryRepository extends JpaRepository<History, Integer> {

    @Query("SELECT h FROM History h WHERE h.pet.id = :petId AND h.status = :status")
    List<History> findByPetIdAndStatus(@Param("petId") int petId, @Param("status") String status);
}

