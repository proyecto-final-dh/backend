package com.company.PetsTest;

import com.company.controller.PetController;
import com.company.enums.PetStatus;
import com.company.model.entity.Breeds;
import com.company.model.entity.Pets;
import com.company.model.entity.Species;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PetsTest {

    @Autowired
    private PetController petController;

    private static Pets newPet;
    private static Pets newPet2;

    @BeforeAll
    public void setup() {
        newPet =  new Pets("Dog", PetStatus.EN_ADOPCION, "MEDIANO", "MACHO", "A friendly dog",2);
        newPet2 = new Pets("Cat", PetStatus.EN_ADOPCION, "MEDIANO", "MACHO", "A friendly cat",3);
    }
    @AfterAll
    public void teardown() {
        newPet = null;
    }

    @Test
    public void testGetAllPets() {
        ResponseEntity<Object> result = petController.createPet(newPet);
        ResponseEntity<Object> result2 = petController.createPet(newPet2);

        List<Pets> petsList = petController.getAllPets(0,9).getContent();
        System.out.println(petsList);
        assertTrue(petsList.size() != 0);

        petController.deletePet(((Pets) result.getBody()).getId());
        petController.deletePet(((Pets) result2.getBody()).getId());
    }

    @Test
    public void testGetPetById() {
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
        ResponseEntity<Object> result = petController.createPet(newPet);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(newPet, result.getBody());

        petController.deletePet(((Pets) result.getBody()).getId());
    }

    @Test
    public void testUpdatePet() {
        ResponseEntity<Object> result = petController.createPet(newPet);
        var bodyResult = ((Pets) result.getBody());
        int id = bodyResult.getId();

        Pets updatedPet = new Pets("CatUpdate", PetStatus.EN_ADOPCION, "PEQUEÑO", "HEMBRA", "A friendly and adopted cat", 3);
        ResponseEntity<Object> resultUpdateResponse = petController.updatePet(id, updatedPet);
        assertEquals(HttpStatus.OK, resultUpdateResponse.getStatusCode());

        Pets resultUpdate = (Pets) resultUpdateResponse.getBody();
        assertEquals(resultUpdate.getName(), updatedPet.getName());

        petController.deletePet(id);
    }

    @Test
    public void testDeletePet() {
        ResponseEntity<Object> result = petController.createPet(newPet);
        var bodyResult = ((Pets) result.getBody());
        int id = bodyResult.getId();

        ResponseEntity<Object> resultDelete = petController.deletePet(id);
        assertEquals(HttpStatus.NO_CONTENT, resultDelete.getStatusCode());
    }
}
