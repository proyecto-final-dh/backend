package com.company.service.interfaces;

import com.company.model.entity.History;

import java.util.List;

public interface IHistoryService {

    History createHistory(History history);

    List<History> getAllHistory();

    History getHistoryById(int id);

    void deleteHistory(int id);

    History updateHistory(int id, History updatedHistory);


}
