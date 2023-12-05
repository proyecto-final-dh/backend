package com.company.controller;

import com.company.model.dto.SaveHistoryDto;
import com.company.model.entity.History;
import com.company.service.HistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {
    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }


    @PostMapping
    public ResponseEntity<Object> createHistory(@Valid @RequestBody SaveHistoryDto item) {
        History response = historyService.createHistory(item);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public List<History> getAllHistory() {
        try {
            return historyService.getAllHistory();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getHistoryById(@PathVariable int id) {
        try {
            History story = historyService.getHistoryById(id);
            return ResponseEntity.ok(story);
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the history");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteHistory(@PathVariable int id) {
        try {
            historyService.deleteHistory(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the history");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateHistory(@PathVariable int id, @RequestBody History history) {
        try {
            History updatedHistory = historyService.updateHistory(id, history);
            return ResponseEntity.ok(updatedHistory);
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the history");
        }
    }

    //TODO: eliminar este get y llamar al servicio getAverageTimeForAdoption dentro del endpoint que está haciendo nat
    @GetMapping("/averageTime")
    public ResponseEntity<Object> getAvergeTimeAdoption() {
        try {
            return ResponseEntity.ok(historyService.getAverageTimeForAdoption());
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the history");
        }
    }

    @GetMapping("/reports/species")
    public ResponseEntity<Object> getReportBySpecies(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                         @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            return ResponseEntity.ok(historyService.getReportBySpecies(startDate, endDate));
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the history");
        }
    }

    @GetMapping("/reports/status")
    public ResponseEntity<Object> getReportByStatus(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                         @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            return ResponseEntity.ok(historyService.getReportByStatus(startDate, endDate));
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the history");
        }
    }

    @GetMapping("/generalReports")
    public ResponseEntity<Object> getFilterPetReports() {
        return ResponseEntity.ok(historyService.filterPetReports());
    }
}