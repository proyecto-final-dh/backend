package com.company.repository;

import com.company.model.entity.Stories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStoriesRepository extends JpaRepository<Stories, Integer> {
}
