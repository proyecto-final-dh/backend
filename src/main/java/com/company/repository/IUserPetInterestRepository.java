package com.company.repository;

import com.company.model.entity.UserPetInterest;
import com.company.model.entity.keys.UserPetInterestId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserPetInterestRepository extends JpaRepository<UserPetInterest, UserPetInterestId> {
    Boolean existsByUserIdAndPetId(int userId, int petId);

    List<UserPetInterest> findAllByUserId(int userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserPetInterest upi WHERE upi.petId = :petId AND upi.userId = :userId")
    void deleteByPetIdAndUserId(@Param("petId") int petId, @Param("userId") int userId);
}
