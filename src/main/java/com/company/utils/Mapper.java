package com.company.utils;

import com.company.model.dto.CreatePetDto;
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
        petWithImagesDto.setBreed_id(pet.getBreed().getId());
        petWithImagesDto.setOwner_id(pet.getUserDetails().getId());
        petWithImagesDto.setImages(images);
        return petWithImagesDto;
    }

    public static Pets mapCreatePetDtoToPet(CreatePetDto createPetDto) {
        Pets pet = new Pets();
        pet.setName(createPetDto.getName());
        pet.setStatus(createPetDto.getStatus());
        pet.setSize(createPetDto.getSize());
        pet.setGender(createPetDto.getGender());
        pet.setDescription(createPetDto.getDescription());
        return pet;
    }
}
