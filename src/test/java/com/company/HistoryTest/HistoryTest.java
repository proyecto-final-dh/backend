package com.company.HistoryTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.company.controller.HistoryController;
import com.company.model.entity.History;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.notNull;

@SpringBootTest
@ActiveProfiles("dev")
public class HistoryTest {

    @Autowired
    private HistoryController historyController;

    @Test
    public void testCreateStory() {
        Date date = new Date(123, 9, 29);
        History newStory = new History(date);
        ResponseEntity<Object> result = historyController.createHistory(newStory);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(newStory.getDate(), ((History) result.getBody()).getDate());
        System.out.println("Id: " + ((History) result.getBody()).getId() + " - Date: " + ((History) result.getBody()).getDate());
        historyController.deleteHistory(((History) result.getBody()).getId());
    }


    @Test
    public void testGetAllStories() {
        History newStory = new History(new Date());
        ResponseEntity<Object> result = historyController.createHistory(newStory);

        History newStory2 = new History(new Date());
        ResponseEntity<Object> result2 = historyController.createHistory(newStory2);

        List<History> historyList = historyController.getAllHistory();
        System.out.println(historyList);
        assertTrue(historyList.size() != 0);

        historyController.deleteHistory(((History) result.getBody()).getId());
        historyController.deleteHistory(((History) result2.getBody()).getId());
    }


    @Test
    public void testGetStoryById() {
        History newStory = new History(new Date());
        ResponseEntity<Object> createResult = historyController.createHistory(newStory);

        int id = ((History) createResult.getBody()).getId();

        ResponseEntity<Object> getResult = historyController.getHistoryById(id);
        System.out.println(getResult);

        assertEquals(HttpStatus.OK, getResult.getStatusCode());
        assertTrue(getResult.getBody() instanceof History);
        assertEquals(((History) getResult.getBody()).getId(), id);

        // Eliminar la entidad después de la prueba
        historyController.deleteHistory(((History) createResult.getBody()).getId());
    }



    @Test
    public void testDeleteStory() {
        History newStory = new History(new Date());
        ResponseEntity<Object> result = historyController.createHistory(newStory);
        var bodyResult = ((History) result.getBody());
        int id = bodyResult.getId();

        ResponseEntity<Object> resultDelete = historyController.deleteHistory(id);
        assertEquals(HttpStatus.NO_CONTENT, resultDelete.getStatusCode());
    }


    @Test
    public void testUpdateHistory() {
        History newHistory = new History(new Date());
        ResponseEntity<Object> createResult = historyController.createHistory(newHistory);
        var createdHistory = (History) createResult.getBody();
        int id = createdHistory.getId();

        History updatedHistory = new History(new Date());
        ResponseEntity<Object> updateResult = historyController.updateHistory(id, updatedHistory);

        assertEquals(HttpStatus.OK, updateResult.getStatusCode());
        History resultUpdate = (History) updateResult.getBody();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        String fechaActualFormateada = dateFormat.format(resultUpdate.getDate());

        String fechaEsperadaFormateada = dateFormat.format(updatedHistory.getDate());
        assertEquals(fechaEsperadaFormateada, fechaActualFormateada);

        historyController.deleteHistory(id);
    }






}