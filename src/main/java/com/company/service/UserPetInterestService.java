package com.company.service;

import com.company.enums.PetStatus;
import com.company.exceptions.ResourceNotFoundException;
import com.company.model.dto.CompleteGetPetDto;
import com.company.model.dto.ImageWithTitle;
import com.company.model.dto.PetWithUserInformationDto;
import com.company.model.dto.UserInformationDTO;
import com.company.model.dto.PetInterestWithOwnerInformationDto;
import com.company.model.dto.UserPetInterestDto;
import com.company.model.entity.Image;
import com.company.model.entity.Pets;
import com.company.model.entity.UserDetails;
import com.company.model.entity.UserPetInterest;
import com.company.repository.IImageRepository;
import com.company.repository.IPetsRepository;
import com.company.repository.IUserDetailsRepository;
import com.company.repository.IUserPetInterestRepository;
import com.company.service.interfaces.IUserPetInterestService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.company.constants.Constants.INTEREST_CAN_ONLY_BE_FOR_PETS_IN_ADOPTION;
import static com.company.constants.Constants.PET_NOT_FOUND;
import static com.company.constants.Constants.USER_INTEREST_ALREADY_EXISTS;
import static com.company.constants.Constants.USER_NOT_FOUND;
import static com.company.utils.Mapper.mapToCompleteGetPetDto;

@Service
public class UserPetInterestService implements IUserPetInterestService {

    private final IUserPetInterestRepository userPetInterestRepository;
    private final IUserDetailsRepository userDetailsRepository;
    private final IPetsRepository petsRepository;
    private final UserService userService;
    private final IImageRepository imageRepository;

    public UserPetInterestService(IUserPetInterestRepository userPetInterestRepository, IUserDetailsRepository userDetailsRepository, IPetsRepository petsRepository, UserService userService, IImageRepository imageRepository) {
        this.userPetInterestRepository = userPetInterestRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.petsRepository = petsRepository;
        this.userService = userService;
        this.imageRepository = imageRepository;
    }

    @Override
    public List<PetWithUserInformationDto> getUserPetListInterests() throws ResourceNotFoundException {
        int userId = getUserId();
        List<PetWithUserInformationDto> listDto = new ArrayList<>();

        List<UserPetInterest> list = userPetInterestRepository.findAllByUserId(userId);

        for (UserPetInterest userPetInterest : list) {
            if (userPetInterest.getPet().getStatus() == PetStatus.EN_ADOPCION) {
                UserInformationDTO userInformationDTO = userService.findById(userPetInterest.getPet().getUserDetails().getUserId());

                Optional<Pets> pets = petsRepository.findById(userPetInterest.getPetId());

                if(pets.isEmpty()){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, PET_NOT_FOUND);
                }

                Optional<List<Image>> image = imageRepository.findByPetId(pets.get().getId());
                List<ImageWithTitle> imageWithTitleList = new ArrayList<>();
                if (image.isPresent()) {
                    for (Image image1 : image.get()) {
                        imageWithTitleList.add(new ImageWithTitle(image1.getId(), image1.getUrl(), image1.getTitle()));
                    }
                }

                CompleteGetPetDto completeGetPetDto = mapToCompleteGetPetDto(pets.get(), imageWithTitleList);

                listDto.add(new PetWithUserInformationDto(completeGetPetDto, userInformationDTO));

            }
        }

        return listDto;
    }

    @Override
    public UserPetInterestDto getUserPetInterest(int petId) {
        int userId = getUserId();

        boolean isInterested = userPetInterestRepository.existsByUserIdAndPetId(userId, petId);

        return new UserPetInterestDto(userId, petId, isInterested);
    }

    @Override
    public PetInterestWithOwnerInformationDto createUserPetInterest(int petId) throws ResourceNotFoundException {
        UserDetails userDetails = getCompleteUserDetails();

        Optional<Pets> pet = petsRepository.findById(petId);

        if (pet.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, PET_NOT_FOUND);
        } else if (pet.get().getStatus() != PetStatus.EN_ADOPCION) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INTEREST_CAN_ONLY_BE_FOR_PETS_IN_ADOPTION);
        }

        boolean isInterested = userPetInterestRepository.existsByUserIdAndPetId(userDetails.getId(), petId);

        if (isInterested) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_INTEREST_ALREADY_EXISTS);
        }

        UserPetInterest userPetInterest = new UserPetInterest(userDetails.getId(), petId, userDetails, pet.get());

        userPetInterestRepository.save(userPetInterest);

        UserInformationDTO userInformationDTO = userService.findById(pet.get().getUserDetails().getUserId());

        return new PetInterestWithOwnerInformationDto(petId, userInformationDTO, true);
    }


    private int getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";

        if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
            userId = jwtAuthToken.getToken().getClaims().get("sub").toString();
        }

        Optional<UserDetails> userDetails = userDetailsRepository.findByUserId(userId);

        if (userDetails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
        }

        return userDetails.get().getId();
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
