package com.company.model.dto;

import com.company.enums.PetStatus;
import com.company.model.entity.Breeds;
import com.company.model.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteGetPetDto {
    private int id;
    private String name;
    private PetStatus status;
    private String size;
    private String gender;
    private String description;
    private Integer age;
    private Breeds breed;
    private Location location;
    private List<ImageWithTitle> images;
}
