package com.company.service.interfaces;

import com.company.enums.PetGender;
import com.company.enums.PetSize;
import com.company.model.dto.CompletePetDto;
import com.company.model.dto.CreatePetDto;
import com.company.model.dto.PetWithImagesDto;
import com.company.enums.PetStatus;
import com.company.model.dto.PetWithUserInformationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.company.model.entity.Pets;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPetService {
    Page<CompletePetDto> findAll(Pageable pageable) throws Exception;
    PetWithUserInformationDto findById(int id) throws Exception;
    Pets update(int id, Pets pets) throws Exception;
    Pets save(Pets pets) throws Exception;
    PetWithImagesDto saveOwnPetWithImages(CreatePetDto pet, MultipartFile[] images) throws Exception;
    PetWithImagesDto saveAdoptivePetWithImages(CreatePetDto pet, MultipartFile[] images) throws Exception;
    void deleteById(int id) throws Exception;

    List<CompletePetDto> findPetsRecommendations(int petId, int limit) throws Exception;
    Page<CompletePetDto> findByLocation(int id, Pageable pageable) throws Exception;
    Page<CompletePetDto> findByOwner(int id, Pageable pageable) throws Exception;
    Page<CompletePetDto> findByStatus(PetStatus status, Pageable pageable) throws Exception;
    Page<CompletePetDto> filterPets(Integer location, Integer species, Integer breed, String size,String status, Pageable pageable) throws Exception;
    Page<CompletePetDto> findByGender(String gender, Pageable pageable) throws Exception;
    Page<CompletePetDto> findBySize(String size, Pageable pageable) throws Exception;

}