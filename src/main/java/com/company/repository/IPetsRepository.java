package com.company.repository;

import com.company.enums.PetGender;
import com.company.enums.PetSize;
import com.company.enums.PetStatus;
import com.company.model.dto.PetStatusUpdateDTO;
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


    @Query(value = "SELECT pets.* " +
            "FROM petPI.pets AS pets " +
            "WHERE pets.owner_id IN " +
            "(SELECT ud.id FROM petPI.user_details AS ud WHERE ud.location_id = :id )",
            countQuery = "SELECT COUNT(*) FROM petPI.pets AS pets " +
                    "WHERE pets.owner_id IN " +
                    "(SELECT ud.id FROM petPI.user_details AS ud WHERE ud.location_id = :id )", nativeQuery = true)
    Page<Pets> findByLocation(@Param("id") int id, Pageable pageable);

    @Query(value = "SELECT pets.* " +
            "FROM petPI.pets AS pets " +
            "WHERE pets.owner_id = :id " +
            "AND pets.status = 'MASCOTA_PROPIA' ",
            countQuery = "SELECT COUNT(*) FROM petPI.pets AS pets " +
            "WHERE pets.owner_id = :id AND pets.status = 'MASCOTA_PROPIA'", nativeQuery = true)
    Page<Pets> findByOwnerAndStatus(@Param("id") int id, Pageable pageable);

    @Query("SELECT DISTINCT NEW com.company.model.dto.PetStatusUpdateDTO(p, h.date) " +
            "FROM Pets p " +
            "INNER JOIN History h ON p.id = h.pet.id " +
            "WHERE h.status = :hStatus AND p.status = :pStatus AND p.userDetails.id = :userId")
    List<PetStatusUpdateDTO> findByStatusAndOwner(@Param("hStatus") String hStatus, @Param("pStatus") PetStatus pStatus, @Param("userId") Integer userId);
    //Se tuvieron que implentar 3 parametros porque History no usó en enum de status, para no romper nada de lo ya construido

    Page<Pets> findByGender(String gender, Pageable pageable);
    Page<Pets> findBySize(String size, Pageable pageable);


}
