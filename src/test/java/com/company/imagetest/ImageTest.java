package com.company.imagetest;

import com.company.model.entity.Image;
import com.company.service.IImageService;
import com.company.service.ImageService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class ImageTest {


    @Autowired
    IImageService imageService;

    @Test
     void testFindAll() {
        Image image= new Image();
        image.setPetID(1);
        image.setUrl("urlDesdeBack");
        imageService.save(image);

        List<Image> result = imageService.findAll();
        assertNotNull(result);
        assertTrue(result.size()>0);

    }

    @Test
    void testSave(){
        Image image= new Image();
        image.setPetID(1);
        image.setUrl("urlDesdeBack");
        Image imageResponde = imageService.save(image);
        assertNotNull(imageResponde);


    }


}
