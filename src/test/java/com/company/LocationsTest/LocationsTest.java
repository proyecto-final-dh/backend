package com.company.LocationsTest;

import com.company.controller.LocationController;
import com.company.model.dto.SaveLocationDTO;
import com.company.model.entity.Location;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class LocationsTest {

    @Autowired
    private LocationController locationController;


    @Test
    void testFindAll(){
        ResponseEntity<List<Location>> response = locationController.findAll();
        List<Location> locations = response.getBody();

        assertNotNull(locations);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSave() {
        //SaveLocationDTO location = new SaveLocationDTO("CountryName", "CityName", "StateName");
        //ResponseEntity<Location> saveLocation = locationController.save(location);
        //assertEquals("CountryName", saveLocation.getBody().getCountry());
        //assertEquals("CityName", saveLocation.getBody().getCity());
        //assertEquals("StateName", saveLocation.getBody().getState());
    }

    @Test
    void testFindById() {
        /*
        SaveLocationDTO location = new SaveLocationDTO("Country", "City", "State");
        ResponseEntity<Location> saveLocation = locationController.save(location);
        int createId = saveLocation.getBody().getId();
        Location findByIdLocation = locationController.findById(createId).getBody();
        assertEquals("City", findByIdLocation.getCity());

         */
    }

    @Test
    void testUpdate() {
        /*
        SaveLocationDTO location = new SaveLocationDTO("MyCountry", "MyCity", "MyState");
        ResponseEntity<Location> saveLocation = locationController.save(location);
        int createId = saveLocation.getBody().getId();
        Location findByIdLocation = locationController.findById(createId).getBody();
        assertEquals("MyCity", findByIdLocation.getCity());
        findByIdLocation.setCity("MyNewCityName");
        ResponseEntity<Location> update = locationController.update(createId, findByIdLocation);
        assertEquals("MyNewCityName", update.getBody().getCity());

         */
    }

    @Test
    void testDeleteById() {
        /*
        SaveLocationDTO location = new SaveLocationDTO("NewCountry", "NewCity", "NewState");
        ResponseEntity<Location> saveLocation = locationController.save(location);
        int createId = saveLocation.getBody().getId();
        Location findByIdLocation = locationController.findById(createId).getBody();
        assertEquals("NewCity", findByIdLocation.getCity());
        ResponseEntity<String> deletedLocation = locationController.deleteById(createId);
        assertEquals(HttpStatus.NO_CONTENT, deletedLocation.getStatusCode());

         */
    }
}
