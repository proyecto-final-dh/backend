package com.company.repository;

import com.company.model.entity.Breeds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBreedsRepository extends JpaRepository<Breeds, Integer> {
}
