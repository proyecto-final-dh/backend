package com.company.repository;

import com.company.model.entity.UserPetInterest;
import com.company.model.entity.keys.UserPetInterestId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserPetInterestRepository extends JpaRepository<UserPetInterest, UserPetInterestId> {
    Boolean existsByUserIdAndPetId(int userId, int petId);

    List<UserPetInterest> findAllByUserId(int userId);
}
