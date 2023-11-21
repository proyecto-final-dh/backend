package com.company.utils;

import com.company.model.dto.CompletePetDto;
import com.company.model.dto.CreatePetDto;
import com.company.model.dto.ImageWithTitle;
import com.company.model.dto.PetWithImagesDto;
import com.company.model.entity.Image;
import com.company.model.entity.Pets;

import java.util.ArrayList;
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

    public static List<ImageWithTitle> mapToImageWithTitleList(List<Image> images){
        List<ImageWithTitle> imageWithTitles = new ArrayList<>();
        for (Image image: images) {
            imageWithTitles.add(mapToImageWithTitleList(image));
        }
        return imageWithTitles;
    }

    public static ImageWithTitle mapToImageWithTitleList(Image image){
        ImageWithTitle imageWithTitles = new ImageWithTitle();
        imageWithTitles.setId(image.getId());
        imageWithTitles.setTitle(image.getTitle());
        imageWithTitles.setUrl(image.getUrl());
        return  imageWithTitles;
    }

    public static CompletePetDto mapToCompletePetDto(Pets pet, List<ImageWithTitle> images){
        CompletePetDto completePetDto = new CompletePetDto();
        completePetDto.setId(pet.getId());
        completePetDto.setName(pet.getName());
        completePetDto.setStatus(pet.getStatus());
        completePetDto.setSize(pet.getSize());
        completePetDto.setGender(pet.getGender());
        completePetDto.setDescription(pet.getDescription());
        completePetDto.setBreed(pet.getBreed());
        completePetDto.setUserDetails(pet.getUserDetails());
        completePetDto.setAge(pet.getAge());
        completePetDto.setImages(images);
        return completePetDto;
    }
}
