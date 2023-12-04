package com.company.repository;

import com.company.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IImageRepository extends JpaRepository<Image, Integer>  {
    Optional<List<Image>> findByPetId(int id);
    Optional<List<Image>> findAllByPetId(int petId);

}
