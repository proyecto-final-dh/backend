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
        petWithImagesDto.setAge(pet.getAge());
        petWithImagesDto.setDescription(pet.getDescription());
        petWithImagesDto.setBreedId(pet.getBreed().getId());
        petWithImagesDto.setOwnerId(pet.getUserDetails().getId());
        petWithImagesDto.setImages(images);
        return petWithImagesDto;
    }

    public static Pets mapCreatePetDtoToPet(CreatePetDto createPetDto) {
        Pets pet = new Pets();
        pet.setName(createPetDto.getName());
        pet.setSize(createPetDto.getSize());
        pet.setGender(createPetDto.getGender());
        pet.setAge(createPetDto.getAge());
        pet.setDescription(createPetDto.getDescription());
        return pet;
    }
}
