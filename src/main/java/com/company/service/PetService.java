package com.company.service;

import com.company.model.dto.ImageWithTitle;
import com.company.model.dto.PetWithImagesDto;
import com.company.model.entity.Image;
import com.company.model.entity.Pets;
import com.company.repository.IImageRepository;
import com.company.repository.IPetsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.company.utils.Mapper.mapPetToPetWithImages;

@AllArgsConstructor
@Service
public class PetService implements  IPetService{

    private IPetsRepository IPetsRepository;
    private BucketImageService bucketImageService;
    private IImageRepository imageRepository;

    public Page<Pets> findAll(Pageable pageable) throws Exception {
        try {
            return IPetsRepository.findAll(pageable);
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas paginadas.");
        }
    }

    public Pets findById(int id) throws Exception {
        try {
            Optional<Pets> pet = IPetsRepository.findById(id);
            if (pet.isPresent()) {
                return pet.get();
            } else {
                throw new Exception("Pet with id " + id + " not found.");
            }
        } catch (Exception e) {
            throw new Exception("Pet with id " + id + " not found.");
        }
    }


    public Pets update(int id, Pets updatedPets) {
        Optional<Pets> existingPet = IPetsRepository.findById(id);

        if (existingPet.isPresent()) {
            Pets pets = existingPet.get();
            pets.setName(updatedPets.getName());
            pets.setStatus(updatedPets.getStatus());
            pets.setGender(updatedPets.getGender());
            pets.setSize(updatedPets.getSize());
            pets.setDescription(updatedPets.getDescription());

            return IPetsRepository.save(pets);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Pet with ID " + id + " does not exist");
        }
    }

    public Pets save(Pets pets) throws Exception {
        if (!pets.getName().isEmpty()) {
            return IPetsRepository.save(pets);
        } else {
            throw new Exception("Pet Name not found");
        }
    }

    @Override
    @Transactional
    public PetWithImagesDto saveWithImages(Pets pet, MultipartFile[] images) {
        Pets savedPet = IPetsRepository.save(pet);
        List<ImageWithTitle> savedImages = bucketImageService.uploadFileWithTitle(images);

        List<Image> imagesToSave = savedImages.stream().map(image -> {
            Image newImage = new Image();
            newImage.setUrl(image.getUrl());
            newImage.setTitle(image.getTitle());
            newImage.setPetID(savedPet.getId());
            return newImage;
        }).toList();

        imageRepository.saveAll(imagesToSave);

        return mapPetToPetWithImages(savedPet, savedImages);

    }

    public void deleteById(int id) throws Exception {
        Optional<Pets> species = IPetsRepository.findById(id);
        if (species.isPresent()) {
            IPetsRepository.deleteById( id);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }


    }


}
