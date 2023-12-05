package com.company.service;

import com.company.model.dto.ReportBySpeciesDto;
import com.company.model.dto.AverageTimeDto;
import com.company.model.dto.ReportByStatusDto;
import com.company.model.dto.GeneralReportsDto;
import com.company.model.dto.SaveHistoryDto;
import com.company.model.entity.History;
import com.company.model.entity.Pets;
import com.company.model.entity.UserDetails;
import com.company.repository.IHistoryRepository;
import com.company.repository.IPetsRepository;
import com.company.repository.IUserDetailsRepository;
import com.company.service.interfaces.IHistoryService;
import com.company.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryService implements IHistoryService {
    private final IHistoryRepository historyRepository;
    private final IPetsRepository petsRepository;

    private final IUserDetailsRepository userDetailsRepository;

    @Autowired
    public HistoryService(IHistoryRepository historyRepository, IPetsRepository petsRepository,IUserDetailsRepository userDetailsRepository) {
        this.historyRepository = historyRepository;
        this.petsRepository = petsRepository;
        this.userDetailsRepository = userDetailsRepository;
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


    public History createHistory(SaveHistoryDto item) {

        Optional<Pets> pet = petsRepository.findById(item.getPetId());
        if (pet.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }

        Optional<UserDetails> userDetails = userDetailsRepository.findById(item.getUserDetailsId());
        if (userDetails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        History newItem = new History(item.getDate());
        newItem.setPet(pet.get());
        newItem.setUserDetails(userDetails.get());


        return historyRepository.save(newItem);
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

    @Override
    public AverageTimeDto getAverageTimeForAdoption() {
        AverageTimeDto averageTimeDto = new AverageTimeDto();
        averageTimeDto.setAverageTime(historyRepository.getAverageTimeForAdoption());
        return averageTimeDto;
    }

    @Override
    public List<ReportBySpeciesDto> getReportBySpecies(LocalDate startDate, LocalDate endDate) {
        var response = historyRepository.getAdoptionsPerMonthAndSpecies(startDate, endDate);
        return Mapper.mapToReportBySpeciesDtoList(response);
    }

    @Override
    public List<ReportByStatusDto> getReportByStatus(LocalDate startDate, LocalDate endDate) {
        var response = historyRepository.getAdoptionsPerMonthAndStatus(startDate, endDate);
        return Mapper.mapToReportByStatusDtoList(response);
    }

    @Override
    public GeneralReportsDto filterPetReports() {
        Object[] response = historyRepository.filterPetReports();

        BigDecimal item1 = (BigDecimal) ((Object[]) response[0])[0];
        BigDecimal item2 = (BigDecimal) ((Object[]) response[0])[1];
        BigDecimal item3 = (BigDecimal) ((Object[]) response[0])[2];

        int enAdopcion = item1.intValue();
        int adoptadas = item2.intValue();
        int conQr = item3.intValue();

        GeneralReportsDto generalReportsDto = new GeneralReportsDto(enAdopcion, adoptadas, conQr);
        return generalReportsDto;
    }

}
