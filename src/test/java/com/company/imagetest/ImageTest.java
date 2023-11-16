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

import java.util.Arrays;
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
        when(imageRepository.save(image)).thenReturn(image);
        Image imageResponde = imageService.save(image);
        System.out.println(imageResponde.getId());
        // Assert
        assertNotNull(imageResponde);
        assertEquals(imageResponde.getPet(), image.getPet());
    }

    @Test
    void testByPetId(){
        int petId = petTest.getId();
        // Crear imágenes de prueba
        Image image1 = new Image();
        image1.setId(1);
        image1.setPet(petTest);
        image1.setUrl("http://example.com/image1.jpg");
        image1.setTitle("Test Image 1");

        Image image2 = new Image();
        image2.setId(2);
        image2.setPet(petTest);
        image2.setUrl("http://example.com/image2.jpg");
        image2.setTitle("Test Image 2");

        List<Image> expectedImages = Arrays.asList(image1, image2);

        // Configurar el mock para devolver las imágenes esperadas
        when(imageRepository.findByPetId(petId)).thenReturn(expectedImages);

        // Ejecutar el servicio que usa el método mockeado
        List<Image> actualImages = imageService.findByPetId(petId);

        // Verificar los resultados
        assertEquals(expectedImages, actualImages, "Las imágenes devueltas deben coincidir con las esperadas.");

    }




}
