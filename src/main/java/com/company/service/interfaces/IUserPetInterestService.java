package com.company.service.interfaces;

import com.company.model.dto.UserPetInterestDto;
import com.company.model.entity.UserPetInterest;

import java.util.List;

public interface IUserPetInterestService {
    UserPetInterestDto getUserPetInterest(int petId);

    List<UserPetInterestDto> getUserPetListInterests();

    UserPetInterestDto createUserPetInterest(int petId);
}
