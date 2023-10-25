package com.company.BreedsTest;

import com.company.controller.BreedsController;
import com.company.model.entity.Breeds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.notNull;

@SpringBootTest
public class BreedsTest {
    @Autowired
    private BreedsController breedsController;
    @BeforeEach
    public void setup() {}

    @Test
    public void testGetAllSpecies() {
        List<Breeds> speciesList = breedsController.getAllSpecies();
        assertTrue(speciesList.size() != 0);
    }

    @Test
    public void testGetSpeciesById() {
        int id = 1;
        ResponseEntity<Object> result = breedsController.getSpeciesById(id);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        notNull(result.getBody().getClass());
        assertTrue(result.getBody() instanceof Breeds);
        assertEquals(((Breeds) result.getBody()).getId(), 1);
    }
}
