package com.company.repository;

import com.company.model.dto.GeneralReportsDto;
import com.company.model.entity.History;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IHistoryRepository extends JpaRepository<History, Integer> {

    @Query(value = "SELECT SUM(CASE WHEN status = 'EN_ADOPCION' THEN 1 ELSE 0 END) AS enAdopcionCount, " +
                    "SUM(CASE WHEN status = 'ADOPTADA' THEN 1 ELSE 0 END) AS adoptadasCount, " +
                    "SUM(CASE WHEN status = 'MASCOTA_PROPIA' THEN 1 ELSE 0 END) AS conQrCount " +
                    "FROM pets", nativeQuery = true)
    Object[] filterPetReports();
}
