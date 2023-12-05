package com.company.model.dto;

import com.company.model.entity.Pets;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class PetStatusUpdateDTO {

    private Pets pet;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp dateCreationPet;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp dateCreationStatus;

    private List<ImageWithTitle> images;


    public PetStatusUpdateDTO(Pets pet, Timestamp dateCreationStatus) {
        this.pet = pet;
        this.dateCreationStatus = dateCreationStatus;
    }
}
