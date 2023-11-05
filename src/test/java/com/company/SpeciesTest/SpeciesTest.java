package com.company.SpeciesTest;

import com.company.controller.SpeciesController;
import com.company.model.entity.Species;
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
public class SpeciesTest {

    @Autowired
    private SpeciesController speciesController;
    @BeforeEach
    public void setup() {}

    @Test
    public void testGetAllSpecies() {
        List<Species> speciesList = speciesController.getAllSpecies();
        assertTrue(speciesList.size() != 0);
    }

    @Test
    public void testGetSpeciesById() {
        int id = 1;
        ResponseEntity<Object> result = speciesController.getSpeciesById(id);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        notNull(result.getBody().getClass());
        assertTrue(result.getBody() instanceof Species);
        assertEquals(((Species) result.getBody()).getId(), 1);
    }

    @Test
    public void testCreateSpecies() {
        Species newSpecies = new Species("hamster");
        ResponseEntity<Object> result = speciesController.createSpecies(newSpecies);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(newSpecies, result.getBody());

        System.out.println( "Id: " + ((Species) result.getBody()).getId() + " - Name: " + ((Species) result.getBody()).getName() );
        //speciesController.deleteSpecies(((Species) result.getBody()).getId());
    }


    @Test
    public void testUpdateSpecies() {
        Species newSpecies = new Species("pajaro");
        ResponseEntity<Object> result = speciesController.createSpecies(newSpecies);
        var bodyResult = ((Species) result.getBody());
        int id = bodyResult.getId();

        Species updatedSpecies = new Species("pajaroUpdate");
        Species resultUpdate = speciesController.updateSpecies(id, updatedSpecies);

        assertEquals(resultUpdate.getName(), updatedSpecies.getName());

        speciesController.deleteSpecies(id);
    }



    @Test
    public void testDeleteSpecies() {
        Species newSpecies = new Species("pajaro");
        ResponseEntity<Object> result = speciesController.createSpecies(newSpecies);
        var bodyResult = ((Species) result.getBody());
        int id = bodyResult.getId();

        ResponseEntity<Object> resultDelete = speciesController.deleteSpecies(id);
        assertEquals(HttpStatus.NO_CONTENT, resultDelete.getStatusCode());

    }

}
