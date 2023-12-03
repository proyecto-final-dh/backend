package com.company.repository;

import com.company.model.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IHistoryRepository extends JpaRepository<History, Integer> {

}
