package com.company.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PetWithUserInformationDto {
    private CopmpleteGetPetDto pet;
    private UserInformationDTO ownerInformation;
}
