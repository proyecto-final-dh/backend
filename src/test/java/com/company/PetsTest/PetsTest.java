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
        List<Pets> petsList = petController.getAllPets(0,9);
        System.out.println(petsList);
        assertTrue(petsList.size() != 0);
    }

    @Test
    public void testGetPetById() {
        int id = 1;
        ResponseEntity<Object> result = petController.getPetById(id);
        System.out.println(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody() instanceof Pets);
        assertEquals(((Pets) result.getBody()).getId(), 1);
    }

    @Test
    public void testCreatePet() {
        Pets newPet = new Pets("Dog", "Available", "Medium", "Male", "A friendly dog");
        ResponseEntity<Object> result = petController.createPet(newPet);

        System.out.println(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(newPet, result.getBody());

        // Cleanup: Delete the created pet
        petController.deletePet(((Pets) result.getBody()).getId());
    }

    @Test
    public void testUpdatePet() {
        Pets newPet = new Pets("Cat4", "Available", "Small", "Female", "A playful cat");
        ResponseEntity<Object> result = petController.createPet(newPet);
        var bodyResult = ((Pets) result.getBody());
        int id = bodyResult.getId();

        Pets updatedPet = new Pets("CatUpdate", "Adopted", "Small", "Female", "A friendly and adopted cat");

        // Llamada para actualizar la mascota y obtener la respuesta
        ResponseEntity<Object> resultUpdateResponse = petController.updatePet(id, updatedPet);

        // Verifica que la actualización se haya realizado con éxito
        assertEquals(HttpStatus.OK, resultUpdateResponse.getStatusCode());

        // Extrae la entidad Pets actualizada del cuerpo de la respuesta
        Pets resultUpdate = (Pets) resultUpdateResponse.getBody();

        // Verifica que el nombre de la mascota actualizada coincida con el nombre proporcionado
        assertEquals(resultUpdate.getName1(), updatedPet.getName1());

        // Cleanup: Delete the updated pet
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
