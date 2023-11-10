package com.company.service;


import com.company.model.entity.Image;
import com.company.repository.IImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ImageService implements IImageService {

    @Autowired
    private final IImageRepository imageRepository;
    @Override
    public List<Image> findAll() {
        return imageRepository.findAll() ;
    }

    public Image save(Image image){
        return imageRepository.save(image);
    }
}
