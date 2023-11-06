package com.company.service;

import com.company.model.dto.SaveHistoryDto;
import com.company.model.entity.History;

import java.util.List;

public interface IHistoryService {

    History createHistory(SaveHistoryDto item);

    List<History> getAllHistory();

    History getHistoryById(int id);

    void deleteHistory(int id);

    History updateHistory(int id, History updatedHistory);


}
