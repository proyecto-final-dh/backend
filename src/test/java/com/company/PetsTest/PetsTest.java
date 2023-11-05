package com.company.PetsTest;

import com.company.controller.PetController;
import com.company.model.entity.Pets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PetsTest {

    @Autowired
    private PetController petController;

    @BeforeEach
    public void setup() {}

    @Test
    public void testGetAllPets() {
        Pets newPet = new Pets("Dog", "Available", "Medium", "Male", "A friendly dog");
        ResponseEntity<Object> result = petController.createPet(newPet);

        Pets newPet2 = new Pets("Cat", "Available", "Medium", "Male", "A friendly dog");
        ResponseEntity<Object> result2 = petController.createPet(newPet2);

        List<Pets> petsList = petController.getAllPets(0,9);
        System.out.println(petsList);
        assertTrue(petsList.size() != 0);

        petController.deletePet(((Pets) result.getBody()).getId());
        petController.deletePet(((Pets) result2.getBody()).getId());
    }

    @Test
    public void testGetPetById() {
        Pets newPet = new Pets("Dog", "Available", "Medium", "Male", "A friendly dog");
        ResponseEntity<Object> createResult = petController.createPet(newPet);

        int id = ((Pets) createResult.getBody()).getId();

        ResponseEntity<Object> getResult = petController.getPetById(id);
        System.out.println(getResult);

        assertEquals(HttpStatus.OK, getResult.getStatusCode());
        assertTrue(getResult.getBody() instanceof Pets);
        assertEquals(((Pets) getResult.getBody()).getId(), id);
        // Eliminar la entidad después de la prueba
        petController.deletePet(((Pets) createResult.getBody()).getId());
    }


    @Test
    public void testCreatePet() {
        Pets newPet = new Pets("Dog", "Available", "Medium", "Male", "A friendly dog");
        ResponseEntity<Object> result = petController.createPet(newPet);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(newPet, result.getBody());

        petController.deletePet(((Pets) result.getBody()).getId());
    }

    @Test
    public void testUpdatePet() {
        Pets newPet = new Pets("Cat4", "Available", "Small", "Female", "A playful cat");
        ResponseEntity<Object> result = petController.createPet(newPet);
        var bodyResult = ((Pets) result.getBody());
        int id = bodyResult.getId();

        Pets updatedPet = new Pets("CatUpdate", "Adopted", "Small", "Female", "A friendly and adopted cat");
        ResponseEntity<Object> resultUpdateResponse = petController.updatePet(id, updatedPet);
        assertEquals(HttpStatus.OK, resultUpdateResponse.getStatusCode());

        Pets resultUpdate = (Pets) resultUpdateResponse.getBody();
        assertEquals(resultUpdate.getName(), updatedPet.getName());

        petController.deletePet(id);
    }


    @Test
    public void testDeletePet() {
        Pets newPet = new Pets("Rabbit", "Available", "Small", "Male", "A cute rabbit");
        ResponseEntity<Object> result = petController.createPet(newPet);
        var bodyResult = ((Pets) result.getBody());
        int id = bodyResult.getId();

        ResponseEntity<Object> resultDelete = petController.deletePet(id);
        assertEquals(HttpStatus.NO_CONTENT, resultDelete.getStatusCode());
    }
}
