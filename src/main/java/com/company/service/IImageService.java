package com.company.service;

import com.company.model.entity.Image;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IImageService {

    List<Image> findAll();
    Image save(Image image);

    void deleteById(Integer id);





}
