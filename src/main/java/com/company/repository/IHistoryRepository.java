package com.company.repository;

import com.company.model.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IHistoryRepository extends JpaRepository<History, Integer> {
    @Query(value = "CALL GetAverageTimeForAdoption()", nativeQuery = true)
    String getAverageTimeForAdoption();

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
}
