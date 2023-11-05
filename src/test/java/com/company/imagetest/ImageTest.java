package com.company.imagetest;

import com.company.model.entity.Image;
import com.company.model.entity.Pets;
import com.company.repository.IImageRepository;
import com.company.repository.IPetsRepository;
import com.company.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

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
    private IPetsRepository petsRepository;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this); // Inicializa los mocks y los injectMocks


        petTest = petsRepository.findAll().get(0);
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
        //when(imageRepository.save(image)).thenReturn(image);
        Image imageResponde = imageService.save(image);
        System.out.println(imageResponde.getId());
        // Assert
        assertNotNull(imageResponde);
        assertEquals(imageResponde.getPet(), image.getPet());
    }




}
