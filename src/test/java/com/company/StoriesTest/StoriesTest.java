package com.company.StoriesTest;

import java.util.Date;
import com.company.controller.BreedsController;
import com.company.controller.StoriesController;
import com.company.model.entity.Stories;
import com.company.service.StoriesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.notNull;

@SpringBootTest
public class StoriesTest {

    @Autowired
    private StoriesController storiesController;

    @Test
    public void testCreateStory() {
        Date date = new Date(123, 9, 29);
        Stories newStory = new Stories(date);
        ResponseEntity<Object> result = storiesController.createStory(newStory);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(newStory.getDate1(), ((Stories) result.getBody()).getDate1());
        System.out.println("Id: " + ((Stories) result.getBody()).getId() + " - Date1: " + ((Stories) result.getBody()).getDate1());
        //storiesController.deleteStory(((Stories) result.getBody()).getId());
    }

}