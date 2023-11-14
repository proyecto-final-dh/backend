package com.company.HistoryTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.company.controller.HistoryController;
import com.company.model.dto.SaveHistoryDto;
import com.company.model.entity.History;
import com.company.model.entity.Pets;
import com.company.model.entity.UserDetails;
import com.company.repository.IHistoryRepository;
import com.company.repository.IPetsRepository;
import com.company.repository.IUserDetailsRepository;
import com.company.service.IHistoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.notNull;


@SpringBootTest
public class HistoryTest {

    @Autowired
    private HistoryController historyController;

    @Autowired
    private IPetsRepository petsRepository;

    @Autowired
    private IUserDetailsRepository userDetailsRepository;

    @Autowired
    private IHistoryRepository historyRepository;

    @Autowired
    private IHistoryService historyService;



    @Test
    void createHistory() {
        SaveHistoryDto historyDto = new SaveHistoryDto(new Date(), 1, 1);
        History createdHistory = historyService.createHistory(historyDto);
        assertNotNull(createdHistory);
        assertTrue(createdHistory.getId() > 0);
        historyService.deleteHistory(createdHistory.getId());
    }

    @Test
    public void testGetAllStories() {
        SaveHistoryDto historyDto = new SaveHistoryDto(new Date(), 1, 1);
        ResponseEntity<Object> result = historyController.createHistory(historyDto);

        SaveHistoryDto historyDto2 = new SaveHistoryDto(new Date(), 1, 1);
        ResponseEntity<Object> result2 = historyController.createHistory(historyDto2);

        List<History> historyList = historyController.getAllHistory();
        System.out.println(historyList);
        assertTrue(historyList.size() != 0);

        historyController.deleteHistory(((History) result.getBody()).getId());
        historyController.deleteHistory(((History) result2.getBody()).getId());
    }

    @Test
    public void testGetStoryById() {
        SaveHistoryDto historyDto = new SaveHistoryDto(new Date(), 1, 1);
        ResponseEntity<Object> createResult = historyController.createHistory(historyDto);

        int id = ((History) createResult.getBody()).getId();

        ResponseEntity<Object> getResult = historyController.getHistoryById(id);
        System.out.println(getResult);

        assertEquals(HttpStatus.OK, getResult.getStatusCode());
        assertTrue(getResult.getBody() instanceof History);
        assertEquals(((History) getResult.getBody()).getId(), id);

        historyController.deleteHistory(((History) createResult.getBody()).getId());
    }

    @Test
    public void testDeleteStory() {
        SaveHistoryDto historyDto = new SaveHistoryDto(new Date(), 1, 1);
        ResponseEntity<Object> result = historyController.createHistory(historyDto);
        var bodyResult = ((History) result.getBody());
        int id = bodyResult.getId();

        ResponseEntity<Object> resultDelete = historyController.deleteHistory(id);
        assertEquals(HttpStatus.NO_CONTENT, resultDelete.getStatusCode());
    }

    @Test
    public void testUpdateHistory() {
        SaveHistoryDto historyDto = new SaveHistoryDto(new Date(), 1, 1);
        ResponseEntity<Object> createResult = historyController.createHistory(historyDto);
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