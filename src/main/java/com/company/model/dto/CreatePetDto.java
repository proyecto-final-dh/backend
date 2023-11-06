package com.company.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePetDto {
    private String name;
    private String status;
    private String size;
    private String gender;
    private String description;
    private Integer breed_id;
    private Integer owner_id;
}
