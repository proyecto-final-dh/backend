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
public class PetWithImagesDto {
    private int id;
    private String name;
    private String statusId;
    private String size;
    private String gender;
    private Integer age;
    private String description;
    private Integer breedId;
    private Integer ownerId;
    private List<ImageWithTitle> images;
}
