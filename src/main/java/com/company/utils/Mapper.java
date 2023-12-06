package com.company.utils;


import com.company.enums.PetStatus;
import com.company.model.dto.ReportBySpeciesDto;
import com.company.model.dto.AdoptionsByDateDto;
import com.company.model.dto.CompletePetDto;
import com.company.model.dto.CopmpleteGetPetDto;
import com.company.model.dto.CreatePetDto;
import com.company.model.dto.ImageWithTitle;
import com.company.model.dto.PetWithImagesDto;
import com.company.model.dto.ReportByStatusDto;
import com.company.model.entity.Image;
import com.company.model.dto.UpdatePetDto;
import com.company.model.entity.Pets;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    public static List<ReportBySpeciesDto> mapToReportBySpeciesDtoList (List<Object[]> responseDB){
        Map<String, ReportBySpeciesDto> reportBySpeciesDtoMap = new HashMap<>();

        for (Object[] row : responseDB) {
            String species = row[0].toString();
            String dateString = row[1].toString();
            LocalDate date = LocalDate.parse(dateString);
            long adoptionsCount = (long) row[2];

            ReportBySpeciesDto reportBySpeciesDto = reportBySpeciesDtoMap.get(species);

            if (reportBySpeciesDto == null) {
                reportBySpeciesDto = new ReportBySpeciesDto();
                reportBySpeciesDto.setSpecies(species);
                reportBySpeciesDto.setResult(new ArrayList<>());
                reportBySpeciesDtoMap.put(species, reportBySpeciesDto);
            }

            AdoptionsByDateDto adoptionsByDateDto = new AdoptionsByDateDto();
            adoptionsByDateDto.setDate(date.format(DateTimeFormatter.ofPattern("MMM yyyy")));
            adoptionsByDateDto.setCount(adoptionsCount);

            reportBySpeciesDto.getResult().add(adoptionsByDateDto);
        }

        return new ArrayList<>(reportBySpeciesDtoMap.values());
    }

    public static List<ReportByStatusDto> mapToReportByStatusDtoList (List<Object[]> responseDB){
        Map<String, ReportByStatusDto> reportByStatusDtoMap = new HashMap<>();

        for (Object[] row : responseDB) {
            String statusString = row[0].toString();
            LocalDate date = LocalDate.parse(row[1].toString());
            long adoptionsCount = (long) row[2];

            PetStatus petStatus = PetStatus.valueOf(statusString);

            ReportByStatusDto reportByStatusDto = reportByStatusDtoMap.get(statusString);

            if (reportByStatusDto == null) {
                reportByStatusDto = new ReportByStatusDto();
                reportByStatusDto.setStatus(petStatus);
                reportByStatusDto.setResult(new ArrayList<>());
                reportByStatusDtoMap.put(statusString, reportByStatusDto);
            }

            AdoptionsByDateDto adoptionsByDateDto = new AdoptionsByDateDto();
            adoptionsByDateDto.setDate(date.format(DateTimeFormatter.ofPattern("MMM yyyy")));
            adoptionsByDateDto.setCount(adoptionsCount);

            reportByStatusDto.getResult().add(adoptionsByDateDto);
        }

        return new ArrayList<>(reportByStatusDtoMap.values());
    }

    public static CopmpleteGetPetDto mapToCompleteGetPetDto(Pets pet, List<ImageWithTitle> images){
        CopmpleteGetPetDto copmpleteGetPetDto = new CopmpleteGetPetDto();
        copmpleteGetPetDto.setId(pet.getId());
        copmpleteGetPetDto.setName(pet.getName());
        copmpleteGetPetDto.setStatus(pet.getStatus());
        copmpleteGetPetDto.setSize(pet.getSize());
        copmpleteGetPetDto.setGender(pet.getGender());
        copmpleteGetPetDto.setDescription(pet.getDescription());
        copmpleteGetPetDto.setBreed(pet.getBreed());
        copmpleteGetPetDto.setLocation(pet.getUserDetails().getLocation());
        copmpleteGetPetDto.setAge(pet.getAge());
        copmpleteGetPetDto.setImages(images);
        return copmpleteGetPetDto;
    }

    public static Pets mapUpdatePetDtoToPet(UpdatePetDto updatePetDto) {
        Pets pet = new Pets();
        pet.setName(updatePetDto.getName());
        pet.setSize(updatePetDto.getSize());
        pet.setGender(updatePetDto.getGender());
        pet.setAge(updatePetDto.getAge());
        pet.setDescription(updatePetDto.getDescription());
        return pet;
    }

}
