package com.company.imagetest;

import com.company.model.entity.Image;
import com.company.repository.IImageRepository;
import com.company.service.ImageService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ImageTest {
    @InjectMocks
    ImageService imageService;
    @Mock
    IImageRepository imageRepository;

    @Test
     void testFindAll() {
        // Arrange
        Image image= new Image();
        image.setPetID(1);
        image.setUrl("url");

        // Act
        when(imageRepository.findAll()).thenReturn(List.of(image));
        List<Image> result = imageService.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());

    }

    @Test
    void testSave(){
        // Arrange
        Image image= new Image();
        image.setPetID(4);
        image.setUrl("urlDesdeBack");

        // Act
        when(imageRepository.save(image)).thenReturn(image);
        Image imageResponde = imageService.save(image);

        // Assert
        assertNotNull(imageResponde);
        assertEquals(imageResponde.getPetID(), image.getPetID());
    }


}
