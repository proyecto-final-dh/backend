package com.company.repository;

import com.company.model.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDetailsRepository extends JpaRepository<UserDetails, String> {
}