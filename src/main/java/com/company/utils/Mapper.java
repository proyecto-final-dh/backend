package com.company.utils;

import com.company.model.dto.ImageWithTitle;
import com.company.model.dto.PetWithImagesDto;
import com.company.model.entity.Pets;

import java.util.List;

public class Mapper {
    public static PetWithImagesDto mapPetToPetWithImages(Pets pet, List<ImageWithTitle> images) {
        PetWithImagesDto petWithImagesDto = new PetWithImagesDto();
        petWithImagesDto.setId(pet.getId());
        petWithImagesDto.setName(pet.getName());
        petWithImagesDto.setStatus(pet.getStatus());
        petWithImagesDto.setSize(pet.getSize());
        petWithImagesDto.setGender(pet.getGender());
        petWithImagesDto.setDescription(pet.getDescription());
        petWithImagesDto.setImages(images);
        return petWithImagesDto;
    }
}
