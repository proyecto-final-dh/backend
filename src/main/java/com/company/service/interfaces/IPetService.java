package com.company.service.interfaces;

import com.company.model.dto.CreatePetDto;
import com.company.model.dto.PetWithImagesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.company.model.entity.Pets;
import org.springframework.web.multipart.MultipartFile;

public interface IPetService {
    Page<Pets> findAll(Pageable pageable) throws Exception;
    Pets findById(int id) throws Exception;
    Pets update(int id, Pets pets) throws Exception;
    Pets save(Pets pets) throws Exception;
    PetWithImagesDto saveWithImages(CreatePetDto pet, MultipartFile[] images) throws Exception;
    void deleteById(int id) throws Exception;
}