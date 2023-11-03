package com.company.repository;

import com.company.model.entity.Breeds;
import com.company.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageRepository extends JpaRepository<Image, Integer>  {


}
