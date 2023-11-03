package com.company.repository;

import com.company.model.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserDetailsRepository extends JpaRepository<UserDetails, Integer> {
    Optional<UserDetails> findByUserId(String userId);
    Boolean existsByUserId(String userId);
}