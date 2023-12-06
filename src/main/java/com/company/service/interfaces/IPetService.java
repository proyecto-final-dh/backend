package com.company.service.interfaces;

import com.company.model.dto.CompletePetDto;
import com.company.model.dto.CreatePetDto;
import com.company.model.dto.PetWithImagesDto;
import com.company.enums.PetStatus;
import com.company.model.dto.PetWithUserInformationDto;
import com.company.model.dto.UpdatePetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.company.model.entity.Pets;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPetService {
    Page<CompletePetDto> findAll(Pageable pageable) throws Exception;
    PetWithUserInformationDto findById(int id) throws Exception;
    PetWithImagesDto update(int id, UpdatePetDto pets, MultipartFile[] newImages) throws Exception;

    Pets updateStatus(int id, String status) throws Exception;

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