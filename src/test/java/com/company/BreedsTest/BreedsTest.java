package com.company.BreedsTest;

import com.company.controller.BreedsController;
import com.company.controller.SpeciesController;
import com.company.model.entity.Breeds;
import com.company.model.entity.Species;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.notNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BreedsTest {
    @Autowired
    private BreedsController breedsController;
    @Autowired
    private SpeciesController speciesController;
    private static Breeds newBreeds;
    private static Species speciesId1;
    @BeforeAll
    public void setup() {
        // Configurar datos de prueba
        int speciesID = 1;
        speciesId1 = (Species) speciesController.getSpeciesById(speciesID).getBody();
        newBreeds = new Breeds("newBreed", speciesId1);
    }
    @AfterAll
    public void teardown() {
        newBreeds = null;
    }
    @Test
    public void testGetAllBreeds() {
        List<Breeds> breedsList = breedsController.getAllBreeds();
        assertTrue(breedsList.size() != 0);
    }
    @Test
    public void testGetBreedsById() {
        int id = 1;
        ResponseEntity<Object> result = breedsController.getBreedsById(id);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(Objects.requireNonNull(result.getBody()).getClass());
        assertTrue(result.getBody() instanceof Breeds);
        assertEquals(((Breeds) result.getBody()).getId(), 1);
    }
    @Test
    public void testCreateBreeds() {
        ResponseEntity<Object> result = breedsController.createBreeds(newBreeds);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        int breedId = ((Breeds) result.getBody()).getId();
        ResponseEntity<Object> resultDelete = breedsController.deleteBreeds(breedId);
        assertEquals(HttpStatus.NO_CONTENT, resultDelete.getStatusCode());
    }
    @Test
    public void testUpdateBreeds() {
        ResponseEntity<Object> result = breedsController.createBreeds(newBreeds);
        var bodyResult = ((Breeds) result.getBody());
        int id = bodyResult.getId();

        Breeds updatedBreeds = new Breeds("pajaroUpdate",speciesId1);

        Breeds resultUpdate = breedsController.updateBreeds(id, updatedBreeds);
        assertEquals(resultUpdate.getName(), updatedBreeds.getName());

        breedsController.deleteBreeds(id);
    }
    @Test
    public void testDeleteBreeds() {

        ResponseEntity<Object> result = breedsController.createBreeds(newBreeds);
        var bodyResult = ((Breeds) result.getBody());
        int breedId = bodyResult.getId();

        ResponseEntity<Object> resultDelete = breedsController.deleteBreeds(breedId);
        assertEquals(HttpStatus.NO_CONTENT, resultDelete.getStatusCode());
    }
}
