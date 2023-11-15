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
        petWithImagesDto.setStatusId(pet.getStatus().getId());
        petWithImagesDto.setSize(pet.getSize());
        petWithImagesDto.setGender(pet.getGender());
        petWithImagesDto.setDescription(pet.getDescription());
        petWithImagesDto.setBreedId(pet.getBreed().getId());
        petWithImagesDto.setOwnerId(pet.getUserDetails().getId());
        petWithImagesDto.setImages(images);
        return petWithImagesDto;
    }

    public static Pets mapCreatePetDtoToPet(CreatePetDto createPetDto) {
        Pets pet = new Pets();
        pet.setName(createPetDto.getName());
        pet.setSize(String.valueOf(createPetDto.getSize()));
        pet.setGender(String.valueOf(createPetDto.getGender()));
        pet.setDescription(String.valueOf(createPetDto.getDescription()));
        return pet;
    }
}
