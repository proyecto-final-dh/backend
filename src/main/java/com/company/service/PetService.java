package com.company.service;


import com.company.enums.PetGender;
import com.company.enums.PetSize;
import com.company.model.entity.History;
import com.company.model.entity.Pets;
import com.company.enums.PetStatus;
import com.company.model.dto.CompletePetDto;
import com.company.model.dto.CreatePetDto;
import com.company.model.dto.ImageWithTitle;
import com.company.model.dto.PetWithImagesDto;
import com.company.model.dto.UpdatePetDto;
import com.company.model.entity.Breeds;
import com.company.model.entity.Image;
import com.company.model.entity.Location;
import com.company.model.entity.Pets;
import com.company.model.entity.UserDetails;
import com.company.repository.IBreedsRepository;
import com.company.repository.IHistoryRepository;
import com.company.repository.IImageRepository;
import com.company.repository.IPetsRepository;
import com.company.repository.IUserDetailsRepository;
import com.company.repository.LocationRepository;
import com.company.service.interfaces.IPetService;
import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.time.Instant;
import java.util.TimeZone;

import static com.company.constants.Constants.BREED_NOT_FOUND;
import static com.company.constants.Constants.EMPTY_IMAGE;
import static com.company.constants.Constants.LOCATION_NOT_FOUND;
import static com.company.constants.Constants.MAXIMUM_IMAGES;
import static com.company.constants.Constants.MAXIMUM_IMAGES_EXCEEDED;
import static com.company.constants.Constants.OWNER_NOT_FOUND;
import static com.company.constants.Constants.PET_AGE_MUST_BE_VALID;
import static com.company.constants.Constants.PET_BREED_REQUIRED;
import static com.company.constants.Constants.PET_DESCRIPTION_REQUIRED;
import static com.company.constants.Constants.PET_GENDER_REQUIRED;
import static com.company.constants.Constants.PET_NAME_REQUIRED;
import static com.company.constants.Constants.PET_NOT_FOUND;
import static com.company.constants.Constants.PET_SIZE_REQUIRED;
import static com.company.constants.Constants.PET_UPDATE_UNAUTHORIZED;
import static com.company.constants.Constants.USER_NOT_FOUND;
import static com.company.constants.Constants.WRONG_PET_GENDER;
import static com.company.constants.Constants.WRONG_PET_SIZE;
import static com.company.constants.Constants.WRONG_PET_UPDATE_STATUS;
import static com.company.utils.Mapper.mapCreatePetDtoToPet;
import static com.company.utils.Mapper.mapPetToPetWithImages;
import static com.company.utils.Mapper.mapToCompletePetDto;
import static com.company.utils.Mapper.mapToImageWithTitleList;
import static com.company.utils.Mapper.mapUpdatePetDtoToPet;

@AllArgsConstructor
@Service
public class PetService implements IPetService {

    private IPetsRepository IPetsRepository;
    private IUserDetailsRepository userDetailsRepository;
    private final LocationRepository locationRepository;
    private BucketImageService bucketImageService;
    private IImageRepository imageRepository;
    private IBreedsRepository breedsRepository;
    private IHistoryRepository historyRepository;


    public Page<CompletePetDto> findAll(Pageable pageable) throws Exception {
        try {
            var petsDB = IPetsRepository.findAll(pageable);
            if (petsDB.isEmpty()) {
                return Page.empty();
            }
            var petsDto = attachImages(petsDB.getContent());
            return new PageImpl<>(petsDto, pageable, petsDB.getTotalElements());
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas paginadas.");
        }
    }

    public CompletePetDto findById(int id) throws Exception {
        try {
            Optional<Pets> pet = IPetsRepository.findById(id);
            if (pet.isPresent()) {
                return attachImages(pet.get());
            } else {
                throw new Exception("Pet with id " + id + " not found.");
            }
        } catch (Exception e) {
            throw new Exception("Pet with id " + id + " not found.");
        }
    }

    @Override
    @Transactional
    public PetWithImagesDto update(int id, UpdatePetDto updatedPet, MultipartFile[] newImages) {
        UserDetails userDetails = getCompleteUserDetails();

        Optional<Pets> oldPet = IPetsRepository.findById(id);

        if (oldPet.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, PET_NOT_FOUND);
        }

        if (!Objects.equals(oldPet.get().getUserDetails().getUserId(), userDetails.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, PET_UPDATE_UNAUTHORIZED);
        }

        PetStatus oldPetStatus = oldPet.get().getStatus();

        if (oldPetStatus.equals(PetStatus.ADOPTADA)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_PET_UPDATE_STATUS);
        }

        int newImagesQuantity = newImages != null ? newImages.length : 0;

        if (updatedPet.getImagesIds() == null) updatedPet.setImagesIds(new ArrayList<>());

        validateBasicUpdatePetData(updatedPet, newImagesQuantity, oldPetStatus.equals(PetStatus.EN_ADOPCION));

        Breeds breed = validateBreeds(updatedPet.getBreedId());

        Optional<List<Image>> imagesToDelete = imageRepository.findAllByPetId(id);
        int imagesToDeleteQuantity = imagesToDelete.map(List::size).orElse(0);

        if (imagesToDelete.isPresent()) {
            for (Integer imageId : updatedPet.getImagesIds()) {
                imagesToDelete.get().removeIf(image -> image.getId() == imageId);
            }
            if (oldPetStatus.equals(PetStatus.EN_ADOPCION) && imagesToDelete.get().size() == imagesToDeleteQuantity && newImagesQuantity == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMPTY_IMAGE);
            }
            imageRepository.deleteAll(imagesToDelete.get());
        }

        Pets newPet = mapUpdatePetDtoToPet(updatedPet);
        newPet.setId(id);
        newPet.setBreed(breed);
        newPet.setUserDetails(userDetails);
        newPet.setStatus(oldPetStatus);

        Pets returnPet = IPetsRepository.save(newPet);

        List<ImageWithTitle> returnImages = new ArrayList<>();

        if (newImages != null && newImages.length > 0) {
            List<ImageWithTitle> savedImages = bucketImageService.uploadFileWithTitle(newImages);
            returnImages.addAll(saveImagesInDatabase(savedImages, returnPet));
        }

        for (Integer imageId : updatedPet.getImagesIds()) {
            Optional<Image> image = imageRepository.findById(imageId);
            if (image.isPresent() && image.get().getPet().getId() == id) {
                ImageWithTitle imageWithTitle = mapToImageWithTitleList(image.get());
                returnImages.add(imageWithTitle);
            }
        }

        return mapPetToPetWithImages(returnPet, returnImages);
    }

    public Pets save(Pets pets) throws Exception {
        validateGender(pets.getGender());
        validateSize(pets.getSize());

        if (!pets.getName().isEmpty()) {
            return IPetsRepository.save(pets);
        } else {
            throw new Exception("Pet Name not found");
        }
    }

    @Override
    @Transactional
    public PetWithImagesDto saveOwnPetWithImages(CreatePetDto pet, MultipartFile[] images) {
        UserDetails userDetails = getCompleteUserDetails();

        validateBasicPetData(pet, false);

        pet.setOwnerId(userDetails.getId());

        Pets fullPet = mapCreatePetDtoToPet(pet);

        fullPet.setBreed(validateBreeds(pet.getBreedId()));
        fullPet.setUserDetails(validateUserDetails(pet.getOwnerId()));
        fullPet.setStatus(PetStatus.MASCOTA_PROPIA);

        Pets savedPet = IPetsRepository.save(fullPet);

        if (images == null || images.length == 0) {
            return mapPetToPetWithImages(savedPet, null);
        }

        List<ImageWithTitle> savedImages = bucketImageService.uploadFileWithTitle(images);

        List<ImageWithTitle> returnImages = saveImagesInDatabase(savedImages, savedPet);

        return mapPetToPetWithImages(savedPet, returnImages);
    }

    @Override
    @Transactional
    public PetWithImagesDto saveAdoptivePetWithImages(CreatePetDto pet, MultipartFile[] images) {
        UserDetails userDetails = getCompleteUserDetails();

        validateBasicPetData(pet, true);

        pet.setOwnerId(userDetails.getId());

        Pets fullPet = mapCreatePetDtoToPet(pet);

        fullPet.setBreed(validateBreeds(pet.getBreedId()));
        fullPet.setUserDetails(validateUserDetails(pet.getOwnerId()));
        fullPet.setStatus(PetStatus.EN_ADOPCION);

        Pets savedPet = IPetsRepository.save(fullPet);

        if(savedPet.getUserDetails() != null){

            Optional<Pets> petIt = IPetsRepository.findById(savedPet.getId());
            Optional<UserDetails> userDetailsTemp = userDetailsRepository.findById(savedPet.getUserDetails().getId());

            if (userDetailsTemp.isPresent() && petIt.isPresent()) {

                History newItem = new History(Date.from(Instant.now()));

                newItem.setPet(petIt.get());
                newItem.setUserDetails(userDetailsTemp.get());
                newItem.setStatus(petIt.get().getStatus().toString());

                historyRepository.save(newItem);
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "userDetails not found");
        }


        List<ImageWithTitle> savedImages = bucketImageService.uploadFileWithTitle(images);
        List<ImageWithTitle> returnImages = saveImagesInDatabase(savedImages, savedPet);

        return mapPetToPetWithImages(savedPet, returnImages);
    }

    public void deleteById(int id) throws Exception {
        Optional<Pets> species = IPetsRepository.findById(id);
        if (species.isPresent()) {
            IPetsRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }
    }


    public List<CompletePetDto> findPetsRecommendations(int petId, int limit) throws Exception {
        Optional<Pets> petOptional = IPetsRepository.findById(petId);

        if (petOptional.isPresent()) {
            Pets pet = petOptional.get();

            List<Pets> recommendations = new ArrayList<>();
            Set<Pets> uniqueRecommendations = new HashSet<>();

            List<Pets> recommendationsAll = IPetsRepository.findPetsRecommendationsAll(petId);
            for (Pets petResult : recommendationsAll) {
                if (petResult.getId() != pet.getId()) {
                    if (uniqueRecommendations.add(petResult)) {
                        recommendations.add(petResult);
                    }
                }
            }

            if (recommendations.size() < limit) {
                int remainingLimit = limit - recommendations.size();

                // Agregar resultados de la segunda consulta a la lista
                List<Pets> specieStatusRecommendations = IPetsRepository.findPetsRecommendationsSpecieStatus(petId);
                for (Pets petResult : specieStatusRecommendations) {
                    if (petResult.getId() != pet.getId()) {
                        if (uniqueRecommendations.add(petResult)) {
                            recommendations.add(petResult);
                            remainingLimit--;
                        }
                    }
                    if (remainingLimit <= 0) {
                        break;
                    }
                }
            }

            if (recommendations.size() < limit) {
                int remainingLimit = limit - recommendations.size();

                // Agregar resultados de la tercera consulta a la lista
                List<Pets> statusRecommendations = IPetsRepository.findPetsRecommendationsStatus(petId);
                for (Pets petResult : statusRecommendations) {
                    if (petResult.getId() != pet.getId()) {
                        if (uniqueRecommendations.add(petResult)) {
                            recommendations.add(petResult);
                            remainingLimit--;
                        }
                    }
                    if (remainingLimit <= 0) {
                        break;
                    }
                }
            }

            recommendations = recommendations.subList(0, Math.min(recommendations.size(), limit));

            var recommendationsDto = attachImages(recommendations);
            return recommendationsDto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }
    }

    @Override
    public Page<CompletePetDto> findByStatus(PetStatus status, Pageable pageable) throws Exception {
        try {
            var petsDB = IPetsRepository.findByStatus(status, pageable);
            if (petsDB.isEmpty()) {
                return Page.empty();
            }
            var petsDto = attachImages(petsDB.getContent());
            return new PageImpl<>(petsDto, pageable, petsDB.getTotalElements());
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas por status.");
        }
    }

    @Override
    public Page<CompletePetDto> filterPets(Integer location, Integer species, Integer breedId, String size, String status, Pageable pageable) throws Exception {
        try {
            Specification<Pets> spec = buildSpecification(location, species, breedId, size, status);
            Page<Pets> petsPage = IPetsRepository.findAll(spec, pageable);
            List<CompletePetDto> petsDto = attachImages(petsPage.getContent());
            return new PageImpl<>(petsDto, pageable, petsPage.getTotalElements());
        } catch (Exception e) {
            throw new Exception("Error al filtrar mascotas");
        }
    }

    public Page<CompletePetDto> findByLocation(int id, Pageable pageable) throws Exception {
        validateLocation(id);
        try {
            var petsDB = IPetsRepository.findByLocation(id, pageable);
            if (petsDB.isEmpty()) {
                return Page.empty();
            }
            var petsDto = attachImages(petsDB.getContent());
            return new PageImpl<>(petsDto, pageable, petsDB.getTotalElements());
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas paginadas.");
        }
    }

    public Page<CompletePetDto> findByOwner(int id, Pageable pageable) throws Exception {
        validateUserDetails(id);
        try {
            var petsDB = IPetsRepository.findByOwner(id, pageable);
            if (petsDB.isEmpty()) {
                return Page.empty();
            }
            var petsDto = attachImages(petsDB.getContent());
            return new PageImpl<>(petsDto, pageable, petsDB.getTotalElements());
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas paginadas.");
        }
    }

    public Page<CompletePetDto> findByGender(String gender, Pageable pageable) throws Exception {
        validateGender(gender);
        try {
            var petsDB = IPetsRepository.findByGender(gender, pageable);
            if (petsDB.isEmpty()) {
                return Page.empty();
            }
            var petsDto = attachImages(petsDB.getContent());
            return new PageImpl<>(petsDto, pageable, petsDB.getTotalElements());
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas por Gender.");
        }
    }

    public Page<CompletePetDto> findBySize(String size, Pageable pageable) throws Exception {
        validateSize(size);
        try {
            var petsDB = IPetsRepository.findBySize(size, pageable);
            if (petsDB.isEmpty()) {
                return Page.empty();
            }
            var petsDto = attachImages(petsDB.getContent());
            return new PageImpl<>(petsDto, pageable, petsDB.getTotalElements());
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas por Size.");
        }
    }


    private Breeds validateBreeds(int id) {
        Optional<Breeds> breeds = breedsRepository.findById(id);
        if (breeds.isPresent()) {
            return breeds.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, BREED_NOT_FOUND);
        }
    }

    private UserDetails validateUserDetails(int id) {
        Optional<UserDetails> userDetails = userDetailsRepository.findById(id);
        if (userDetails.isPresent()) {
            return userDetails.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, OWNER_NOT_FOUND);
        }
    }

    private void validateBasicPetData(CreatePetDto pet, boolean isForAdoption) {
        if (pet.getName() == null || pet.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_NAME_REQUIRED);
        }
        if (pet.getBreedId() == null || pet.getBreedId() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_BREED_REQUIRED);
        }
        if (pet.getAge() != null && pet.getAge() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_AGE_MUST_BE_VALID);
        }

        if (pet.getGender() != null && !PetGender.isValidGender(pet.getGender())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_PET_GENDER);
        }

        if (pet.getSize() != null && !PetSize.isValidSize(pet.getSize())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_PET_SIZE);
        }

        if (isForAdoption) {
            if (pet.getGender() == null || pet.getGender().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_GENDER_REQUIRED);
            }
            if (pet.getSize() == null || pet.getSize().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_SIZE_REQUIRED);
            }
            // TODO: Revalidate this case
            if (pet.getDescription() == null || pet.getDescription().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_DESCRIPTION_REQUIRED);
            }
        }
    }

    private void validateBasicUpdatePetData(UpdatePetDto pet, Integer newImagesQuantity, boolean isForAdoption) {
        if (pet.getName() == null || pet.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_NAME_REQUIRED);
        }
        if (pet.getBreedId() == null || pet.getBreedId() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_BREED_REQUIRED);
        }
        if (pet.getAge() != null && pet.getAge() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_AGE_MUST_BE_VALID);
        }
        if (pet.getGender() != null && !PetGender.isValidGender(pet.getGender())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_PET_GENDER);
        }
        if ((newImagesQuantity + pet.getImagesIds().size()) > MAXIMUM_IMAGES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MAXIMUM_IMAGES_EXCEEDED);
        }
        if (pet.getSize() != null && !PetSize.isValidSize(pet.getSize())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_PET_SIZE);
        }
        if (isForAdoption) {
            if (pet.getGender() == null || pet.getGender().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_GENDER_REQUIRED);
            }
            if (pet.getSize() == null || pet.getSize().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_SIZE_REQUIRED);
            }
            if (pet.getDescription() == null || pet.getDescription().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PET_DESCRIPTION_REQUIRED);
            }
            if (newImagesQuantity == 0 && pet.getImagesIds().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMPTY_IMAGE);
            }
        }
    }

    private List<ImageWithTitle> saveImagesInDatabase(List<ImageWithTitle> images, Pets pet) {
        List<Image> imagesToSave = images.stream().map(image -> {
            Image newImage = new Image();
            newImage.setUrl(image.getUrl());
            newImage.setTitle(image.getTitle());
            newImage.setPet(pet);
            return newImage;
        }).toList();

        List<Image> savedImages = imageRepository.saveAll(imagesToSave);

        return savedImages.stream().map(image -> {
            ImageWithTitle newImage = new ImageWithTitle();
            newImage.setId(image.getId());
            newImage.setUrl(image.getUrl());
            newImage.setTitle(image.getTitle());
            return newImage;
        }).toList();
    }

    private Specification<Pets> buildSpecification(Integer location, Integer species, Integer breedId, String size, String status) {
        Specification<Pets> spec = Specification.where(null);

        if (location != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Pets, UserDetails> userDetailsJoin = root.join("userDetails");
                Join<UserDetails, Location> locationJoin = userDetailsJoin.join("location");
                return cb.equal(locationJoin.get("id"), location);
            });
        }

        if (species != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("breed").get("species").get("id"), species));
        }

        if (breedId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("breed").get("id"), breedId));
        }

        if (size != null && !size.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("size"), size));
        }

        if (status != null && !status.isEmpty()) {
            PetStatus petStatus = PetStatus.valueOf(status);
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), petStatus));
        }

        return spec;
    }


    private Location validateLocation(int id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            return location.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, LOCATION_NOT_FOUND);
        }
    }

    private List<CompletePetDto> attachImages(List<Pets> pets) {
        List<CompletePetDto> petsDto = new ArrayList<>();
        for (Pets pet : pets) {
            var images = imageRepository.findByPetId(pet.getId());
            if (images.isPresent()) {
                List<ImageWithTitle> imagesPets = mapToImageWithTitleList(images.get());
                petsDto.add(mapToCompletePetDto(pet, imagesPets));
            }
        }
        return petsDto;
    }

    private CompletePetDto attachImages(Pets pet) {
        CompletePetDto petDto = new CompletePetDto();
        var images = imageRepository.findByPetId(pet.getId());
        if (images.isPresent()) {
            List<ImageWithTitle> imagesPets = mapToImageWithTitleList(images.get());
            petDto = mapToCompletePetDto(pet, imagesPets);
        }
        return petDto;
    }

    private void validateGender(String gender) {
        if (gender != null && !gender.isEmpty() && !PetGender.isValidGender(gender)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_PET_GENDER);
        }
    }

    private void validateSize(String size) {
        if (size != null && !size.isEmpty() && !PetSize.isValidSize(size)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_PET_SIZE);
        }
    }

    private UserDetails getCompleteUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";

        if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
            userId = jwtAuthToken.getToken().getClaims().get("sub").toString();
        }

        Optional<UserDetails> userDetails = userDetailsRepository.findByUserId(userId);

        if (userDetails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
        }

        return userDetails.get();
    }
}
