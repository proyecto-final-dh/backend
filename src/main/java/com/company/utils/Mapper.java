package com.company.utils;

import com.company.enums.PetGender;
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
        petWithImagesDto.setGender(pet.getGender().getId());
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
        // TODO: Verificar que se guarda correctamente. Si se usa string no hace falta validarlo (ya que se deberia haber hecho en el service). En caso de usar enum, habria que ver si no rompe que este vacio.
        pet.setGender(PetGender.getGenderById(createPetDto.getGender()));
        pet.setDescription(String.valueOf(createPetDto.getDescription()));
        return pet;
    }
}
