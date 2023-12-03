package com.company.service.interfaces;

import com.company.exceptions.ResourceNotFoundException;
import com.company.model.dto.PetInterestWithOwnerInformationDto;
import com.company.model.dto.UserPetInterestDto;

import java.util.List;

public interface IUserPetInterestService {
    UserPetInterestDto getUserPetInterest(int petId);

    List<PetInterestWithOwnerInformationDto> getUserPetListInterests() throws ResourceNotFoundException;

    PetInterestWithOwnerInformationDto createUserPetInterest(int petId) throws ResourceNotFoundException;
}
