package com.company.model.dto;

import com.company.enums.PetStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatePetDto {
    private String name;
    private PetStatus status;
    private String size;
    private String gender;
    private String description;
    private Integer breedId;
    private Integer ownerId;
}
