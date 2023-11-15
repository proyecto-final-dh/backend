package com.company.service.interfaces;

import com.company.model.dto.CreatePetDto;
import com.company.model.dto.PetWithImagesDto;
import com.company.enums.PetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.company.model.entity.Pets;
import org.springframework.web.multipart.MultipartFile;

public interface IPetService {
    Page<Pets> findAll(Pageable pageable) throws Exception;
    Pets findById(int id) throws Exception;
    Pets update(int id, Pets pets) throws Exception;
    Pets save(Pets pets) throws Exception;
    PetWithImagesDto saveOwnPetWithImages(CreatePetDto pet, MultipartFile[] images) throws Exception;
    PetWithImagesDto saveAdoptivePetWithImages(CreatePetDto pet, MultipartFile[] images) throws Exception;
    void deleteById(int id) throws Exception;
    Page<Pets> findByStatus(PetStatus status, Pageable pageable) throws Exception;
    Page<Pets> filterPets(String location, String species, Integer breed, String size,String status, Pageable pageable) throws Exception;
}