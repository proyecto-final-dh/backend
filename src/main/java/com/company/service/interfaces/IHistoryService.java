package com.company.service.interfaces;

import com.company.model.dto.GeneralReportsDto;
import com.company.model.dto.ReportBySpeciesDto;
import com.company.model.dto.AverageTimeDto;
import com.company.model.dto.ReportByStatusDto;
import com.company.model.dto.SaveHistoryDto;
import com.company.model.entity.History;

import java.time.LocalDate;
import java.util.List;

public interface IHistoryService {

    History createHistory(SaveHistoryDto item);

    List<History> getAllHistory();

    History getHistoryById(int id);

    void deleteHistory(int id);

    History updateHistory(int id, History updatedHistory);

    AverageTimeDto getAverageTimeForAdoption();

    List<ReportBySpeciesDto> getReportBySpecies(LocalDate startDate, LocalDate endDate);
    List<ReportByStatusDto> getReportByStatus(LocalDate startDate, LocalDate endDate);

    GeneralReportsDto filterPetReports();
}
