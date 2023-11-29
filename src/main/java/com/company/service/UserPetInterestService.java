package com.company.service;

import com.company.enums.PetStatus;
import com.company.model.dto.UserPetInterestDto;
import com.company.model.entity.Pets;
import com.company.model.entity.UserDetails;
import com.company.model.entity.UserPetInterest;
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

import static com.company.constants.Constants.PET_NOT_FOUND;
import static com.company.constants.Constants.USER_INTEREST_ALREADY_EXISTS;
import static com.company.constants.Constants.USER_NOT_FOUND;

@Service
public class UserPetInterestService implements IUserPetInterestService {

    private final IUserPetInterestRepository userPetInterestRepository;
    private final IUserDetailsRepository userDetailsRepository;
    private final IPetsRepository petsRepository;

    public UserPetInterestService(IUserPetInterestRepository userPetInterestRepository, IUserDetailsRepository userDetailsRepository, IPetsRepository petsRepository) {
        this.userPetInterestRepository = userPetInterestRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.petsRepository = petsRepository;
    }

    @Override
    public List<UserPetInterestDto> getUserPetListInterests() {
        int userId = getUserId();
        List<UserPetInterestDto> listDto = new ArrayList<>();

        List<UserPetInterest> list = userPetInterestRepository.findAllByUserId(userId);

        for (UserPetInterest userPetInterest : list) {
            if (userPetInterest.getPet().getStatus() == PetStatus.EN_ADOPCION) {
                listDto.add(new UserPetInterestDto(userPetInterest.getUserId(), userPetInterest.getPetId(), true));
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
    public UserPetInterestDto createUserPetInterest(int petId) {
        UserDetails userDetails = getCompleteUserDetails();

        Optional<Pets> pet = petsRepository.findById(petId);

        if (pet.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, PET_NOT_FOUND);
        }

        boolean isInterested = userPetInterestRepository.existsByUserIdAndPetId(userDetails.getId(), petId);

        if (isInterested) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_INTEREST_ALREADY_EXISTS);
        }

        UserPetInterest userPetInterest = new UserPetInterest(userDetails.getId(), petId, userDetails, pet.get());

        userPetInterestRepository.save(userPetInterest);

        return new UserPetInterestDto(userDetails.getId(), petId, true);
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
