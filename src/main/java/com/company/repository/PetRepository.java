package com.company.repository;

import com.company.model.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Pet findByNameAndStatusAndSizeAndGenderAndDescription(String name, String status, String size, String gender,String description);


}
