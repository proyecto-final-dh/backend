package com.company.service;

import com.company.model.entity.History;
import com.company.repository.IHistoryRepository;
import com.company.service.interfaces.IHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService implements IHistoryService {
    private final IHistoryRepository historyRepository;

    @Autowired
    public HistoryService(IHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }



    public List<History> getAllHistory() {
        return historyRepository.findAll();
    }


    public History getHistoryById(int id) {
        Optional<History> story = historyRepository.findById(id);
        if (story.isPresent()) {
            return story.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "History not found");
        }
    }



    @Override
    public History createHistory(History history) {
        System.out.println(history);
        if (history.getDate() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Date is required");
        }

        return historyRepository.save(history);
    }


    public void deleteHistory(int id) {
        Optional<History> story = historyRepository.findById(id);
        if (story.isPresent()) {
            historyRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "History not found");
        }
    }


    public History updateHistory(int id, History updatedHistory) {
        Optional<History> existingHistory = historyRepository.findById(id);

        if (existingHistory.isPresent()) {
            History history = existingHistory.get();
            history.setDate(updatedHistory.getDate());

            return historyRepository.save(history);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "History with ID " + id + " does not exist");
        }
    }


}
