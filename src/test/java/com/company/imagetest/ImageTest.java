package com.company.imagetest;

import com.company.model.entity.Image;
import com.company.model.entity.Pets;
import com.company.repository.IImageRepository;
import com.company.repository.IPetsRepository;
import com.company.service.IImageService;
import com.company.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ImageTest {
    @InjectMocks
    ImageService imageService;
    @Mock
    IImageRepository imageRepository;

    private Pets petTest;


    @Autowired
    private IPetsRepository mascotaRepository;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this); // Inicializa los mocks y los injectMocks

        // Inicializa la mascota para usarla en todos los tests
        petTest = new Pets();
        petTest.setId(1);
        petTest.setName1("Firulais");

    }


    @Test
     void testFindAll() {
        // Arrange
        Image image= new Image();
        image.setPet(petTest);
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
        image.setPet(petTest);
        image.setUrl("urlDesdeBack");

        // Act
        when(imageRepository.save(image)).thenReturn(image);
        Image imageResponde = imageService.save(image);

        // Assert
        assertNotNull(imageResponde);
        assertEquals(imageResponde.getPet(), image.getPet());
    }


}
