package com.company.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdatePetDto {
    private String name;
    private String size;
    private String gender;
    private Integer age;
    private String description;
    private List<Integer> imagesIds;
    private Integer breedId;
    private Integer ownerId;
}
