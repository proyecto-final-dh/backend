package com.company.repository;

import com.company.enums.PetStatus;
import com.company.model.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.time.LocalDate;


public interface IHistoryRepository extends JpaRepository<History, Integer> {

    @Query("SELECT h FROM History h WHERE h.pet.id = :petId AND h.status = :status")
    List<History> findByPetIdAndStatus(@Param("petId") int petId, @Param("status") String status);
    @Query(value = "CALL GetAverageTimeForAdoption()", nativeQuery = true)
    Double getAverageTimeForAdoption();

    @Query(value = "CALL AdoptionsPerMonthAndSpecies(:startDate, :endDate)", nativeQuery = true)
    List<Object[]> getAdoptionsPerMonthAndSpecies(
            @Param("startDate") LocalDate fechaInicio,
            @Param("endDate") LocalDate fechaFin
    );

    @Query(value = "CALL AdoptionsPerMonthAndStatus(:startDate, :endDate)", nativeQuery = true)
    List<Object[]> getAdoptionsPerMonthAndStatus(
            @Param("startDate") LocalDate fechaInicio,
            @Param("endDate") LocalDate fechaFin
    );

    @Query(value = "SELECT SUM(CASE WHEN status = 'EN_ADOPCION' THEN 1 ELSE 0 END) AS enAdopcionCount, " +
                    "SUM(CASE WHEN status = 'ADOPTADA' THEN 1 ELSE 0 END) AS adoptadasCount, " +
                    "SUM(CASE WHEN status = 'MASCOTA_PROPIA' THEN 1 ELSE 0 END) AS conQrCount " +
                    "FROM pets", nativeQuery = true)
    Object[] filterPetReports();
}
